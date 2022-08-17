package com.dao.impl;

import com.dao.BaseDao;
import com.dao.BookDao;
import com.pojo.Book;

import java.sql.Connection;
import java.util.List;

/**
 * @description: BookDao 实现类
 */
//BaseDao<Book> 指明是操作 Book 类
public class BookDaoImpl extends BaseDao<Book> implements BookDao {
    @Override
    public void insert(Connection connection, Book book) {
        String sql = "insert into book_info(bookid,bookname,bookautor,bookprice,booknum) values(?,?,?,?,?)";
        update(connection, sql, book.getBookid(), book.getBookname(), book.getBookautor(), book.getBookprice(), book.getBooknum());
    }
    
    @Override
    public void deleteByBookid(Connection connection, String bookid) {
        String sql = "delete from book_info where bookid=?";
        update(connection, sql, bookid);
    }
    
    @Override
    public void update(Connection connection, Book book) {
        String sql = "update book_info set bookname=?,bookautor=?,bookprice=?,booknum=? where bookid=?";
        update(connection, sql, book.getBookname(), book.getBookautor(), book.getBookprice(), book.getBooknum(), book.getBookid());
    }
    
    @Override
    public List<Book> getAll(Connection connection) {
        String sql = "select bookid,bookname,bookautor,bookprice,booknum from book_info";
        return query(connection, sql);
    }
    
    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from book_info";
        return getValue(connection, sql);
    }
    
    @Override
    public Double getMaxBookprice(Connection connection) {
        String sql = "select max(bookprice) from book_info";
        return getValue(connection, sql);
    }
}
