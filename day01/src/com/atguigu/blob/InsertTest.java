package com.atguigu.blob;

import com.atguigu.util.JdbcUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/*
* 使用preparedStatement实现批量数据的操作
*
* update、delete本身就具有批量操作的效果
* 此时的批量操作，主要指的是批量插入。使用PreparedStatement如何实现高效的批量插入
*
* 题目：向goods插入20000条数据
* 1、创建表
*   CREATE TABLE goods(
      id INT PRIMARY KEY AUTO_INCREMENT,
      NAME VARCHAR(25)
    )
*   方法一：使用Statement
*       Connection conn = JDBCUtil.getConnection();
*       Statement statement = conn.createStatement();
*       for(int i = 1; i < 20000; i++) {
*           String sql = "insert into goods(name) value ('name_" + i + "')";
*           statement.execute(sql);
*       }
*
* */
public class InsertTest {

    /**
    * Description: 批量插入的方式二
    * date: 2021/11/12 13:56
    * @author: lichao
    * @since JDK 1.8
    */
    @Test
    public void insertTest() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JdbcUtil.getConnection();
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i < 20000; i++) {
                ps.setObject(1, "name" + i);
                ps.execute();
            }
            Long end = System.currentTimeMillis();
            System.out.println("花费的时间：" + (end - start)); // 20000: 1562106
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps);
        }
    }

    /**
     * Description: 批量插入方式三：
     * 1、addBatch()、executeBatch()、clearBatch()
     * 2、mysql默认是关闭批量处理的，我们需要通过一个参数，让MySQL开启批量处理的支持
     *          ?rewriteBatchedStatements=true 写在配置文件的url后面
     * 3、使用更新的MySQL驱动，mysql-connector-java-5.1.37-bin.jar
    */
    @Test
    public void insertTest2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JdbcUtil.getConnection();
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name" + i);
                // 1、"攒sql"
                ps.addBatch();
                if(i % 500 == 0) {
                    // 2、执行sql
                    ps.executeBatch();
                    // 3、清除sql
                    ps.clearBatch();
                }
            }
            Long end = System.currentTimeMillis();
            System.out.println("花费的时间：" + (end - start)); // 20000: 217628
                                                               // 1000000: 217628
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps);
        }
    }

    // 批量插入的方式四：设置不允许自动提交数据
    @Test
    public void insertTest3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JdbcUtil.getConnection();
            // 设置不允许自动提交数据
            conn.setAutoCommit(false);
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name" + i);
                // 1、"攒sql"
                ps.addBatch();
                if(i % 500 == 0) {
                    // 2、执行sql
                    ps.executeBatch();
                    // 3、清除sql
                    ps.clearBatch();
                }
            }
            // 提交数据
            conn.commit();
            Long end = System.currentTimeMillis();
            System.out.println("花费的时间：" + (end - start)); // 20000: 217628
                                                               // 1000000: 35363
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps);
        }
    }
}
