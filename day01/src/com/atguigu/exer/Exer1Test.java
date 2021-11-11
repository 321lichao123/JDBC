package com.atguigu.exer;

import com.atguigu.util.JdbcUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Exer1Test {
    public static void main(String[] args) {
        testUpdate();
    }
    public static void testUpdate() {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入用户名:");
        String name = sc.next();
        System.out.print("请输入邮箱:");
        String email = sc.next();
        System.out.print("请输入生日:");
        String birthday = sc.next();

        String sql = "insert into customers(name, email, birth) values (?, ?, ?)";
        int update = update(sql, name, email, birthday);
        if(update > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    // 通用增删改操作
    public static int update(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            /*
            * ps.execute()
            * 如果执行的是查询操作，则返回结果true
            * 如果执行的是增、删、改，则返回结果false
            * */
//            return ps.execute();
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps);
        }
        return 0;
    }

}
