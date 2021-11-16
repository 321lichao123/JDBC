package com.atguigu4.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

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

    /**
    * Description: 使用DBCP数据库连接池技术获取数据库链接<br/>
    * @author: 12468<br/>
    * @date: 2021/11/15 23:32<br/>
    * @param:<br/>
    * @return:
    */
    private static DataSource source;
    static {
        try {
            Properties pros = new Properties();
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws SQLException {
        return source.getConnection();
    }
    /**
    * Description: 使用druid数据库连接池技术获取数据库链接
    * date: 2021/11/16 14:27
    * @author: lichao
    * @since JDK 1.8
    */
    private static DataSource source1;
    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception {
        return source1.getConnection();

    }

    public static void closeSource(Connection conn, PreparedStatement ps) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if (ps != null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
