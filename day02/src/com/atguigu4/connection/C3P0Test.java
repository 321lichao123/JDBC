package com.atguigu4.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Test {
    // 方法一：
    @Test
    public void testGetConnection() throws Exception {
        // 获取连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        // 填写链接MySQL的基本信息
        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" );
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("admin@123");
        // 通过相关的参数，对数据库连接池进行管理
        // 设置初始时数据库连接池中链接数
        cpds.setInitialPoolSize(10);
        Connection conn = cpds.getConnection();
        System.out.println(conn);
        // 销毁链接池，一般不推荐销毁
        // DataSources.destroy(cpds);
    }
    // 方法二：
    @Test
    public void testGetConnection1() throws SQLException {
        ComboPooledDataSource cbps = new ComboPooledDataSource("hellc3p0");
        Connection conn = cbps.getConnection();
        System.out.println(conn);
    }
}
