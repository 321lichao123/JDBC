package com.atguigu.prepareStatement;

import com.atguigu.bean.Order;
import com.atguigu.util.JdbcUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/*
    针对表的字段名与类的属性名不一致的情况：
        1、必须声明sql时，使用类的属性名来命名字段的别名
        2、使用ResultSetMetaData时，需要使用getColumnLabel()方法替换getColumnName()方法，来获取别名
            补充说明：
                如果sql中没有给字段起别名，getColumnLabel()方法也是可以获取列名的

 */

public class OrderForQuery {
    @Test
    public void test1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JdbcUtil.getConnection();
            String sql = "select order_id, order_name, order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            res = ps.executeQuery();
            if(res.next()) {
                int orderId = (int) res.getObject(1);
                String orderName = (String) res.getObject(2);
                Date orderDate = (Date) res.getObject(3);

                Order order = new Order(orderId, orderName, orderDate);
                System.out.println(order);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps,res);
        }
    }

    @Test
    public void test2() {
        String sql = "select order_id AS orderId, order_name AS orderName, order_date AS orderDate from `order` where order_name = ?";
        Order order = testOrderQuery(sql, "AA");
        System.out.println(order);
    }

    public Order testOrderQuery(String sql, Object ...args) {
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
            // 通过ResultSetMetaData来获取数据集的列数
            int columnCount = metaData.getColumnCount();
            if(res.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    // 获取数据集的列名
                    // 获取类的列名: getColumnName()  不推荐使用
                    // 获取类的别名：getColumnLabel()
                    String columnName = metaData.getColumnLabel(i + 1);
                    // 获取数据集的列名对应的值
                    Object columnValue = res.getObject(i + 1);
                    // 通过反射order对象的columnName赋值为column Value的值
                    Field field = Order.class.getDeclaredField(columnName);
                    field.setAccessible(true); // 为了保证私有可以访问
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps, res);
        }
        return null;
    }
}
