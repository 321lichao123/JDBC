package com.atguigu.util;


import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
* Description: jdbc的工具类
* date: 2021/11/8 17:22
* @author: lichao 
* @since JDK 1.8
*/
public class JdbcUtil {

    /**
    * Description: 获取connection链接
    * date: 2021/11/8 17:26
    * @author: lichao
    * @since JDK 1.8
    */
    public static Connection  getConnection() throws Exception {
        // 1、读取配置项文件的基本信息
        InputStream systemResourceAsStream = ClassLoader.getSystemClassLoader().getSystemResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(systemResourceAsStream);
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String diverClass = properties.getProperty("diverClass");

        // 2、加载驱动
        Class.forName(diverClass);

        // 3、获取链接
        Connection connection = DriverManager.getConnection(url, user, password);

        return connection;
    }


    public static void closeResource(Connection con, PreparedStatement ps) {
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if(con != null)
                con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void closeResource(Connection con, PreparedStatement ps, ResultSet res) {
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if(con != null)
                con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if(res != null)
                res.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
