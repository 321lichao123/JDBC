package com.atguigu.blob;

import com.atguigu.bean.Customer;
import com.atguigu.util.JdbcUtil;
import org.junit.Test;

import java.io.*;
import java.sql.*;

public class BlobTest {
    @Test
    public void test1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtil.getConnection();
            String sql = "insert into customers (name, email, birth, photo) values (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, "苍井空");
            ps.setObject(2, "cjk@126.com");
            ps.setObject(3, "1991-01-01");
            FileInputStream stream = new FileInputStream(new File("11.jpg"));
            ps.setObject(4, stream);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResource(conn, ps);
        }
    }

    @Test
    public void test2() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        InputStream photoBinaryStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            conn = JdbcUtil.getConnection();
            String sql = "select id, name, email, birth, photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 21);
            res = ps.executeQuery();
            if(res.next()) {
                //        方法一：
                //        int id = res.getInt(1);
                //        String name = res.getString(2);
                //        String email = res.getString(3);
                //        String photo = res.getString(4);
                //        方法二：
                int id = res.getInt("id");
                String name = res.getString("name");
                String email = res.getString("email");
                Date birth = res.getDate("birth");
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                // 将blob类型的字段下载下来，以文件的形式保存到本地
                Blob photo = res.getBlob("photo");
                photoBinaryStream = photo.getBinaryStream();
                fileOutputStream = new FileOutputStream("22.jpg");
                byte[] bytes = new byte[1024];
                int len;
                while((len = photoBinaryStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (photoBinaryStream != null)
                    photoBinaryStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JdbcUtil.closeResource(conn,ps, res);
        }

    }
}
