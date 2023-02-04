package com.jdbc.dao;

import com.jdbc.pojo.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @description: 用于规范对Book表的常用操作
 */
public interface BookDao {
    
    /**
     * 添加Book对象到数据库
     *
     * @param connection
     * @param book
     */
    void insert(Connection connection, Book book) throws SQLException;
    
    /**
     * 根据id删除数据表中的记录
     *
     * @param connection
     * @param bookid
     */
    void deleteById(Connection connection, String bookid) throws SQLException;
    
    /**
     * 针对内存中的Book对象，修改数据表中的指定记录
     *
     * @param connection
     * @param book
     */
    void update(Connection connection, Book book) throws SQLException;
    
    /**
     * 查询数据表中的所有记录
     *
     * @param connection
     * @return
     */
    List<Book> getAll(Connection connection) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException;
    
    /**
     * 返回数据表中记录的总数
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
    Double getMaxPrice(Connection connection);
}