package com.atguigu4.util;

import com.atguigu.bean.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class TestNewJDBCUtil {
    @Test
    public void testGetConn() throws SQLException {
        Connection conn = JDBCUtil.getConnection1();
        System.out.println(conn);
    }
    @Test
    public void test() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnection1();
            String sql = "select id, name, email, birth from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            ResultSet res = ps.executeQuery();
            ResultSetMetaData metaData = res.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (res.next()) {
                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = res.getObject(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(ps != null)
                    ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
}
