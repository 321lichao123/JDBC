package com.atguigu5.dbutils;

import com.atguigu2.bean.Customer;
import com.atguigu4.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryRunningTest {
    // 测试查询
    @Test
    public void testInsert() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtil.getConnection3();
            String sql = "insert into customers(name, email, birth) values (?, ?, ?)";
            int count = runner.update(conn, sql, "小泽玛丽亚", "xzm@126.com", "2021-11-16");
            System.out.println("添加成功" + count + "条");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, null);
        }
    }

    // 测试查询
    // BeanHandler: 是ResultSetHandle接口实现类，用于封装表中的一条数据
    @Test
    public void testQuery() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtil.getConnection3();
            String sql = "select id, name, email, birth from customers where id = ?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 21);
            System.out.println("customer = " + customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, null);
        }
    }

    //
    @Test
    public void testQuery1() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtil.getConnection3();
            String sql = "select id, name, email, birth from customers where id < ?";
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> list = runner.query(conn, sql, handler, 21);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, null);
        }
    }

    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtil.getConnection3();
            String sql = "select id, name, email, birth from customers where id = ?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 21);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, null);
        }
    }

    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtil.getConnection3();
            String sql = "select id, name, email, birth from customers where id < ?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 21);
            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, null);
        }
    }

    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtil.getConnection3();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            Long count = (Long) runner.query(conn, sql, handler);
            System.out.println("count = " + count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, null);
        }
    }

    // 自定义ResultSetHandle中实现方式
    @Test
    public void testQuery6() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtil.getConnection3();
            String sql = "select id, name, email, birth from customers where id = ?";
            ResultSetHandler<Customer> result = new ResultSetHandler() {
                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
                    try {
                        if (resultSet.next()) {
                            ResultSetMetaData metaData = resultSet.getMetaData();
                            int columnCount = metaData.getColumnCount();
                            Customer customer = new Customer();
                            for (int i = 0; i < columnCount; i++) {
                                String columnLabel = metaData.getColumnLabel(i + 1);
                                Object columnValue = resultSet.getObject(i + 1);
                                Field field = Customer.class.getDeclaredField(columnLabel);
                                field.setAccessible(true);
                                field.set(customer, columnValue);
                            }
                            return customer;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            Customer customer = runner.query(conn, sql, result, 13);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, null);
        }
    }
}
