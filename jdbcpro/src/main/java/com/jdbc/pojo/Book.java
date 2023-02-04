package com.jdbc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 书本实体类
 *
 * ORM 编程思想（object relation mapping）
 * 一个数据表 对应 一个 Java 类
 * 表中的一个记录 对应 Java 类的一个对象
 * 表中的一个字段 对应 Java 类的一个属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;
    private String name;
    private String author;
    private Double price;
    private Integer num;
}