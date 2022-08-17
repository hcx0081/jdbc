package com.dao;

import com.pojo.Book;

import java.sql.Connection;
import java.util.List;

/**
 * @description: 用于规范对于 Book 表的常用操作
 */
public interface BookDao {
    
    /**
     * 添加 Book 对象到数据库
     *
     * @param connection
     * @param book
     */
    void insert(Connection connection, Book book);
    
    /**
     * 根据 bookid 删除表中的数据
     *
     * @param connection
     * @param bookid
     */
    void deleteByBookid(Connection connection, String bookid);
    
    /**
     * 针对内存中的 book 对象，去修改数据表中的指定的记录
     *
     * @param connection
     * @param book
     */
    void update(Connection connection, Book book);
    
    /**
     * 查询所有记录
     *
     * @param connection
     * @return
     */
    List<Book> getAll(Connection connection);
    
    /**
     * 返回数据表中数据的总数
     *
     * @param connection
     * @return
     */
    Long getCount(Connection connection);
    
    /**
     * 返回数据表中最大的价格
     *
     * @param connection
     * @return
     */
    Double getMaxBookprice(Connection connection);
    
}
