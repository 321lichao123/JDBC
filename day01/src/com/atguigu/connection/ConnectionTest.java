package com.atguigu.connection;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    @Test
    public void testConnection1() throws SQLException {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        // jdbc:mysql：协议
        // localhost: ip地址
        // 3306：MySQL的默认端口号
        // test: 需要链接数据库
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "admin@123");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }
}
