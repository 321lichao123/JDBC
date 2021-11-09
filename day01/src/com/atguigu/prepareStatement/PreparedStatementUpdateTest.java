package com.atguigu.prepareStatement;

/*
* 使用preparedStatement来替换statement，实现数据表的增删改查(crud)操作
*
* */

import com.atguigu.connection.ConnectionTest;
import com.atguigu.util.JdbcUtil;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PreparedStatementUpdateTest {
    @Test
    public void test1() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 1、创建数据库的链接
            connection = JdbcUtil.getConnection();


            // 4、预编译sql语句，返回PrepareStatement的实力
            String sql = "insert into customers(name, email, birth) values (?, ? , ?)"; // ?：占位符
            preparedStatement = connection.prepareStatement(sql);

            // 5、填充占位符
            preparedStatement.setString(1, "苍进空");
            preparedStatement.setString(2, "cjk@gmail.com");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = dateFormat.parse("1000-01-01");
            preparedStatement.setDate(3, new java.sql.Date(parse.getTime()));

            // 6、执行操作
            preparedStatement.execute();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7、关闭资源
            JdbcUtil.closeResource(connection, preparedStatement);
        }
    }

    @Test
    public void test2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1、获取数据库的链接
            conn = JdbcUtil.getConnection();

            // 2、预编译sql语句，返回PreparedStatement的实力
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);

            // 3、填充占位符
            ps.setObject(1, "莫扎克");
            ps.setObject(2, 18);

            // 4、执行语句
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5、关闭资源
            JdbcUtil.closeResource(conn, ps);
        }
    }

    @Test
    public void testUpdate() {
//        String sql = "delete from customers where id = ?";
//        update(sql, 12);
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql, "DD", 2);
    }

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

}
