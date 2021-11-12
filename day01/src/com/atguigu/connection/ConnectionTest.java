package com.atguigu.connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    @Test
    public void testConnection1() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();
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

    // 方法二：对方法进行迭代：在如下的的程序中不出现第三方的的api是的程序具有更好的移植性
    @Test
    public void testConnection2() throws Exception {
        // 1、使用Driver实现类对象，使用反射
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        // 2、提供要链接的数据库
        String str = "jdbc:mysql://localhost:3306/test";

        // 3、提供链接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "admin@123");
        Connection connect = driver.connect(str, info);
        System.out.println(connect);

    }

    // 方法三： 使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception {
        // 获取Driver实现类的对象
        Class<?> aclass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver diver = (Driver) aclass.newInstance();

        // 提供另外三个的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "admin@123";

        // 注册驱动
        DriverManager.registerDriver(diver);

        // 获取链接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    // 方法四
    @Test
    public void testConnection4() throws Exception {
        // 1、设置三个参数
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "admin@123";

        // 获取Driver实现类的对象
        // 为什么不需要注册驱动：因为MySQL的Drive的实现类中添加了静态代码块进行判断了，如果没有注册会自动注册的
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 获取链接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    // 方法五：将数据库链接需要的4个基本信息声明在配置文件中，通过读取配置文件的方法获取链接
    @Test
    public void testConnection5() throws Exception {
        // 1、读取配置文件中的4个基本信息
        InputStream resourceAsStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(resourceAsStream);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String diverClass = properties.getProperty("diverClass");

        // 加载驱动
        Class.forName(diverClass);

        // 获取链接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }
}
