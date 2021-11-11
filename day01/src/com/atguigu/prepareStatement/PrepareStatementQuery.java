package com.atguigu.prepareStatement;

import com.atguigu.bean.Customer;
import com.atguigu.util.JdbcUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class PrepareStatementQuery {
    @Test
    public void test() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JdbcUtil.getConnection();

            String sql = "select id, name, email, birth from customers where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setObject(1, 1);

            res = ps.executeQuery();

            if(res.next()) {
                int id = res.getInt(1);
                String name = res.getString(2);
                String email = res.getString(3);
                Date birth = res.getDate(4);
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps, res);
        }
    }

    @Test
    public void testQueryFromCustomers() {
        String sql = "select id, name, email, birth from customers where id = ?";
        Customer customer = queryFromCustomers(sql, 2);
        System.out.println(customer);

    }

    public Customer queryFromCustomers(String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JdbcUtil.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            res = ps.executeQuery();
            // 获取结果的原数据：ResultSetMetaData
            ResultSetMetaData metaData = res.getMetaData();
            // 通过ResultSetMetaData来获取结果集中列数
            int columnCount = metaData.getColumnCount();
            if(res.next()) {
                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    // 获取数据集的列名
                    String columnName = metaData.getColumnName(i + 1);
                    // 获取数据集的值
                    Object columnValue = res.getObject(i + 1);
                    // 通过反射给customer对象的columnName属性赋值为columnValue
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps, res);
        }
        return null;
    }
}
