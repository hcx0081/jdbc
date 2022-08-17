package com.pojo;

/**
 * @description: 书本实体类
 *
 * ORM 编程思想（object relation mapping）
 * 一个数据表 对应 一个 java 类
 * 表中的一个记录 对应 java 类的一个对象
 * 表中的一个字段 对应 java 类的一个属性
 */
public class Book {
    private String bookid;
    private String bookname;
    private String bookautor;
    private Double bookprice;
    private int booknum;

    public Book() {
    }

    public Book(String bookid, String bookname, String bookautor, Double bookprice, int booknum) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.bookautor = bookautor;
        this.bookprice = bookprice;
        this.booknum = booknum;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookautor() {
        return bookautor;
    }

    public void setBookautor(String bookautor) {
        this.bookautor = bookautor;
    }

    public Double getBookprice() {
        return bookprice;
    }

    public void setBookprice(Double bookprice) {
        this.bookprice = bookprice;
    }

    public int getBooknum() {
        return booknum;
    }

    public void setBooknum(int booknum) {
        this.booknum = booknum;
    }


    @Override
    public String toString() {
        return "Book{" +
                "bookid='" + bookid + '\'' +
                ", bookname='" + bookname + '\'' +
                ", bookautor='" + bookautor + '\'' +
                ", bookprice=" + bookprice +
                ", booknum=" + booknum +
                '}';
    }
}
