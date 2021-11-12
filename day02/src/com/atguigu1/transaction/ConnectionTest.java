package com.atguigu1.transaction;

import com.atguigu1.util.JdbcUtil;
import org.junit.Test;

import java.sql.Connection;

public class ConnectionTest {
    @Test
    public void test1() throws Exception {
        Connection conn = JdbcUtil.getConnection();
        System.out.println(conn);
    }

}
