package com.atguigu1.transaction;

import com.atguigu1.util.JdbcUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
*
* 哪些操作会导致数据的自动提交？
*   > DDL操作一旦执行，都会自动提交
*       > set autocommit = false对于DDL不生效
*   > DML默认情况下，一旦执行，就会自动提交
*       > 但是可以通过设置set autocommit = false的方式取消DML操作的自动提交
*   > 默认在关闭链接的时候，会自动提交数据
*
* */

public class TransactionTest {

    // *******************************************************************
    /*
    * 针对数据表user_table来说：
    *   AA用户给BB用户转账100
    *
    * */
    @Test
    public void testUpdate() {
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");

        // 模拟网络异常
        System.out.println(10 / 0);

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2,"BB");

        System.out.println("转账成功");
    }

    // 通用增删改操作 version --1.0
    public void update(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1、获取链接
            conn = JdbcUtil.getConnection();

            // 2、预编译数据库
            ps = conn.prepareStatement(sql);

            // 3、填充占位符
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // 4、执行语句
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5、关闭资源
            JdbcUtil.closeResource(conn, ps);
        }
    }

    // ***************************************************************************
    @Test
    public void test2() {
        Connection conn = null;
        try {
            conn = JdbcUtil.getConnection();

            // 取消数据的自动提交
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(conn, sql1, "AA");

            // 模拟网络异常
            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(conn, sql2,"BB");

            // 提交数据
            conn.commit();
            System.out.println("转账成功");
        } catch (Exception e) {
            // 3、回滚数据
            try {
                conn.rollback();
                System.out.println("转账失败");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
//            e.printStackTrace();
        } finally {
            try {
                // 修改其为自动提交数据
                // 主要针对使用数据库连接池的使用
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JdbcUtil.closeResource(conn, null);
        }

    }
    // 通用增删改操作 version --2.0
    public void update(Connection conn ,String sql, Object ...args) {
        PreparedStatement ps = null;
        try {
            // 2、预编译数据库
            ps = conn.prepareStatement(sql);
            // 3、填充占位符
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4、执行语句
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5、关闭资源
            JdbcUtil.closeResource(null, ps);
        }
    }

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JdbcUtil.getConnection();
        // 获取隔离级别
        System.out.println(conn.getTransactionIsolation());
        // 设置隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        // 取消自动提交数据
        conn.setAutoCommit(false);
        String sql = "select * from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JdbcUtil.getConnection();
        // 取消自动提交数据
        conn.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?";
        update(conn, sql, 5000, "CC");
        Thread.sleep(15000);

        System.out.println("修改结束");
    }

    // 通用查询操作 version -- 2.0
    public <T>T getInstance(Connection conn, Class<T> clazz,String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            // 2、预编译
            ps = conn.prepareStatement(sql);
            // 3、填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4、执行sql语句
            res = ps.executeQuery();
            // 5、获取结果的元数据:ResultSetMetaData
            ResultSetMetaData metaData = res.getMetaData();
            // 6、获取元数据的总列数
            int columnCount = metaData.getColumnCount();
            // 7、通过ResultSet结果集的next()指针判断下个是否存在下个字段
            if(res.next()) {
                T t = clazz.newInstance();
                // 8、遍历总列数获取各个列的列名喝值
                for (int i = 0; i < columnCount; i++) {
                    // 9、通过元数据的ResultSetMetaData获取列表名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    // 10、通过ResultSet获取列对应的值
                    Object columnValue = res.getObject(i + 1);
                    // 通过反射将值放入上面的实例中
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(null, ps, res);
        }
        return null;
    }
}
