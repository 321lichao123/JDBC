package com.atguigu4.connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * ClassName: DruidTest
 *
 * @Author: lichao
 * @Date: 2021/11/15
 * @Description:
 */
public class DruidTest {
    @Test
    public void testGetConnection() throws Exception {
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println("conn = " + conn);
        
    }
}
