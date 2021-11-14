package com.atguigu2.dao;

import com.atguigu1.util.JdbcUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BaseDAO
 *
 * @Author: lichao
 * @Date: 2021/11/14
 * @Description:
 */
public abstract class BaseDAO {
    // 通用增删改操作 version --2.0
    public void update(Connection conn ,String sql, Object ...args) {
        PreparedStatement ps = null;
        try {
            // 2、预编译数据库
            ps = conn.prepareStatement(sql);
            // 3、填充占位符
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4、执行语句
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5、关闭资源
            JdbcUtil.closeResource(null, ps);
        }
    }
    // 通用查询操作 version -- 2.0
    public <T>T getInstance(Connection conn, Class<T> clazz, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
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
            JdbcUtil.closeResource(null, ps, res);
        }
        return null;
    }

    // 通用查询多条数据的操作
    public <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
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

    // 用于查询特殊值的通用方法
    public <E> E getValue(Connection conn, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            res = ps.executeQuery();
            if(res.next()) {
                return (E) res.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(null, ps, res);
        }
        return null;
    }
}
