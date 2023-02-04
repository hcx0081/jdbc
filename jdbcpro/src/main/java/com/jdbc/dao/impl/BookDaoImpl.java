package com.jdbc.dao.impl;

import com.jdbc.dao.BaseDao;
import com.jdbc.dao.BookDao;
import com.jdbc.pojo.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @description: BookDao实现类
 */
// BaseDao<Book>指明操作Book数据表
public class BookDaoImpl extends BaseDao<Book> implements BookDao {
    @Override
    public void insert(Connection connection, Book book) throws SQLException {
        String sql = "insert into book(id, name, author, price, num) values(?, ?, ?, ?, ?)";
        update(connection, sql, book.getId(), book.getName(), book.getAuthor(), book.getPrice(), book.getNum());
    }
    
    @Override
    public void deleteById(Connection connection, String bookid) throws SQLException {
        String sql = "delete from book where id = ?";
        update(connection, sql, bookid);
    }
    
    @Override
    public void update(Connection connection, Book book) throws SQLException {
        String sql = "update book set name = ?, author = ?, price = ?, num = ? where id = ?";
        update(connection, sql, book.getName(), book.getAuthor(), book.getPrice(), book.getNum(), book.getId());
    }
    
    @Override
    public List<Book> getAll(Connection connection) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String sql = "select id ,name, author, price, num from book";
        return query(connection, sql);
    }
    
    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from book";
        return getValue(connection, sql);
    }
    
    @Override
    public Double getMaxPrice(Connection connection) {
        String sql = "select max(price) from book";
        return getValue(connection, sql);
    }
}