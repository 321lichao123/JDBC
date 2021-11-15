package com.atguigu4.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ClassName: DBCPTest
 *
 * @Author: lichao
 * @Date: 2021/11/15
 * @Description:
 */
public class DBCPTest {
    /**
    * Description: 测试DBCP的数据库连接池<br/>
    * @author: 12468<br/>
    * @date: 2021/11/15 23:02<br/>
    * @param:<br/>
    * @return:
    */
    // 方式一：不推荐
    @Test
    public void testGetConnection() throws SQLException {
        // 创建了DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        // 设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("admin@123");

        // 设置其他设计数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);

        // 获取链接
        Connection conn = source.getConnection();

        System.out.println("conn = " + conn);
    }

    // 方式二：推荐：使用配置文件
    @Test
    public void testGetConnection1() throws Exception {
        Properties pros = new Properties();
        // 获取配置文件的方式一：
         InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        // 获取配置文件的方式二：
        // FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println("conn = " + conn);
    }
}
