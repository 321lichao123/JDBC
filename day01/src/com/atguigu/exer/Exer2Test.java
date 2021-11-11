package com.atguigu.exer;

import com.atguigu.util.JdbcUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

public class Exer2Test {
    public static void main(String[] args) {
//        insert();
//        query();
        deletedByExamCard1();
    }

    /**
    * Description: 删除数据
    * date: 2021/11/11 15:19
    * @author: lichao
    * @since JDK 1.8
    */
    public static void deletedByExamCard() {
        System.out.println("请输入学生的考号：");
        Scanner sc = new Scanner(System.in);
        String examCard = sc.next();
        String sql = "select FlowID AS flowID, Type AS type, IDCard, ExamCard AS examCard, StudentName as name, Location AS location, Grade AS grade from examstudent where ExamCard = ?";
        Student student = testQuery(Student.class, sql, examCard);
        if(student != null) {
            String deletedSql = "DELETE from examstudent where ExamCard = ?";
            int update = testUpdate(deletedSql, examCard);
            if(update > 0) {
                System.out.println("删除成功");
            }
        } else {
            System.out.println("查无此人， 请重新输入！");
        }
    }

    public static void deletedByExamCard1() {
        System.out.println("请输入学生的考号：");
        Scanner sc = new Scanner(System.in);
        String examCard = sc.next();
        String deletedSql = "DELETE from examstudent where ExamCard = ?";
        int update = testUpdate(deletedSql, examCard);
        if(update > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("查无此人， 请重新输入！");
        }
    }
    /**
    * Description: 查询操作
    * date: 2021/11/11 15:18
    * @author: lichao
    * @since JDK 1.8
    */
    public static void query() {
        System.out.println("请选择你要输入的类型：");
        System.out.println("a:准考证");
        System.out.println("b:身份证");
        Scanner sc = new Scanner(System.in);
        String type = sc.next();
        if("a".equalsIgnoreCase(type)) {
            System.out.println("请输入准考证号：");
            String examCard = sc.next();
            String sql = "select FlowID AS flowID, Type AS type, IDCard, ExamCard AS examCard, StudentName as name, Location AS location, Grade AS grade from examstudent where ExamCard = ?";
            Student student = testQuery(Student.class, sql, examCard);
            if(student != null) {
                System.out.println(student);
            } else {
                System.out.println("查无此人，请重新进入程序！");
            }
        } else if ("b".equalsIgnoreCase(type)) {
            System.out.println("请输入身份证号：");
            String IDCard = sc.next();
            String sql = "select FlowID AS flowID, Type AS type, IDCard, ExamCard AS examCard, StudentName as name, Location AS location, Grade AS grade from examstudent where IDCard = ?";
            Student student = testQuery(Student.class, sql, IDCard);
            if(student != null) {
                System.out.println(student);
            } else {
                System.out.println("查无此人，请重新进入程序！");
            }
        } else {
            System.out.println("您输入的有误！请重新进入程序。");
        }
    }

    /**
    * Description: 插入数据
    * date: 2021/11/11 15:18
    * @author: lichao
    * @since JDK 1.8
    */
    public static void insert() {
        Scanner sc = new Scanner(System.in);
        System.out.print("4级/6级：");
        int Type = sc.nextInt();
        System.out.print("身份证：");
        String IDCard = sc.next();
        System.out.print("准考证：");
        String ExamCard = sc.next();
        System.out.print("考生姓名：");
        String StudentName = sc.next();
        System.out.print("所在城市：");
        String Location = sc.next();
        System.out.print("考试成绩：");
        String Grade = sc.next();

        String sql = "insert into examstudent(Type, IDCard, ExamCard, StudentName, Location, Grade) values (?, ?, ?, ?, ?, ?)";
        int update = testUpdate(sql, Type, IDCard, ExamCard, StudentName, Location, Grade);
        if(update > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    /**
    * Description: 通用的增删改操作
    * date: 2021/11/11 14:37
    * @author: lichao
    * @since JDK 1.8
    */
    public static int testUpdate(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps);
        }
        return 0;
    }

    /**
    * Description: 通用查询操作
    * date: 2021/11/11 14:48
    * @author: lichao
    * @since JDK 1.8
    */
    public static <T>T testQuery(Class<T> clazz, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            res = ps.executeQuery();
            ResultSetMetaData metaData = res.getMetaData();
            int columnCount = metaData.getColumnCount();
            if(res.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnValue = res.getObject(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps, res);
        }
        return null;
    }
}
