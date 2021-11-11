package com.atguigu.statement;


/**
* Description: 演示使用PreparedStatement替换Statement，解决SQL注入的问题
* date: 2021/11/10 18:20
* @author: lichao 
* @since JDK 1.8
 *
 * PrepareStateMent除了解决拼串问题和SQL的注入问题还有的好处是：
 *  1、PreparedStatement还可以操作Blob数据，而statement不行
 *  2、PreparedStatement还可以高效的的批量操作数据
 *  3、preparedStatement为什么可以解决SQL的注入问题？
 *      因为preparedStatement存在一个预编译的步骤
*/
public class PreparedStatemenetTest {


}
