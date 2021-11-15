package com.atguigu4.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtil {
    /**
    * Description: 使用C3P0的数据库连接池技术
    * date: 2021/11/15 15:34
    * @author: lichao 
    * @since JDK 1.8
    */
    // 数据库链接吃只需提供一个即可
    private static ComboPooledDataSource cbps = new ComboPooledDataSource("hellc3p0");
    public static Connection getConnection1() throws SQLException {
        Connection conn = cbps.getConnection();
        return conn;
    }
}
