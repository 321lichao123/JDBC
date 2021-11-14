package com.atguigu2.dao;

import com.atguigu2.bean.Customer;

import java.sql.Connection;

/**
 * ClassName: CustomerDAO
 *
 * @Author: lichao
 * @Date: 2021/11/14
 * @Description: 规范操作customer的数据库
 */
public interface CustomerDAO {
    /**
    * Description: 向customer插入数据<br/>
    * @author: 12468<br/>
    * @date: 2021/11/14 22:20<br/>
    * @param:<br/>
    * @return:
    */
    public void insert(Connection conn, Customer cust);
    /**
    * Description: 根据id删除customer中的数据<br/>
    * @author: 12468<br/>
    * @date: 2021/11/14 22:23<br/>
    * @param:<br/>
    * @return:
    */
    public void deleteById(Connection conn, int id);

    /**
    * Description: 更新customer数据<br/>
    * @author: 12468<br/>
    * @date: 2021/11/14 22:24<br/>
    * @param:<br/>
    * @return:
    */
    public void update(Connection conn, Customer cust);


}
