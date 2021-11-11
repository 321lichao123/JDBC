package com.atguigu.prepareStatement;

import com.atguigu.bean.Customer;
import com.atguigu.bean.Order;
import com.atguigu.util.JdbcUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreparedStatementQueryTest {

    @Test
    public void testGetForList() {
        String sql = "select id, name, email from customers where id < ?";
        List<Customer> customers = getForList(Customer.class, sql, 4);
        customers.forEach(System.out:: println);

        String sql1 = "select order_id AS orderId, order_name AS orderName from `order` where order_id < ?";
        List<Order> orders = getForList(Order.class, sql1, 3);
        orders.forEach(System.out:: println);
    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            // 1、获取链接
            conn = JdbcUtil.getConnection();
            // 2、预编译
            ps = conn.prepareStatement(sql);
            // 3、填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4、执行sql语句
            res = ps.executeQuery();
            // 5、获取结果的元数据:ResultSetMetaData
            ResultSetMetaData metaData = res.getMetaData();
            // 6、获取元数据的总列数
            int columnCount = metaData.getColumnCount();
            // 7、通过ResultSet结果集的next()指针判断下个是否存在下个字段
            // 创建一个list集合
            ArrayList<T> list = new ArrayList<>();
            while (res.next()) {
                T t = clazz.newInstance();
                // 8、遍历总列数获取各个列的列名喝值
                for (int i = 0; i < columnCount; i++) {
                    // 9、通过元数据的ResultSetMetaData获取列表名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    // 10、通过ResultSet获取列对应的值
                    Object columnValue = res.getObject(i + 1);
                    // 通过反射将值放入上面的实例中
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps, res);
        }
        return null;
    }

    @Test
    public void testGetInstance() {
        String sql = "select id, name, email, birth from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql, 1);
        System.out.println(customer);

        String sql1 = "select order_id AS orderId, order_name AS orderName from `order` where order_name = ?";
        Order order = getInstance(Order.class, sql1, "AA");
        System.out.println(order);
    }


    public <T>T getInstance(Class<T> clazz,String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            // 1、获取链接
            conn = JdbcUtil.getConnection();
            // 2、预编译
            ps = conn.prepareStatement(sql);
            // 3、填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4、执行sql语句
            res = ps.executeQuery();
            // 5、获取结果的元数据:ResultSetMetaData
            ResultSetMetaData metaData = res.getMetaData();
            // 6、获取元数据的总列数
            int columnCount = metaData.getColumnCount();
            // 7、通过ResultSet结果集的next()指针判断下个是否存在下个字段
            if(res.next()) {
                T t = clazz.newInstance();
                // 8、遍历总列数获取各个列的列名喝值
                for (int i = 0; i < columnCount; i++) {
                    // 9、通过元数据的ResultSetMetaData获取列表名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    // 10、通过ResultSet获取列对应的值
                    Object columnValue = res.getObject(i + 1);
                    // 通过反射将值放入上面的实例中
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps, res);
        }
        return null;
    }
}
