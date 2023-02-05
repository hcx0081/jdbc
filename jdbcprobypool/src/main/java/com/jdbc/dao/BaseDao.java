package com.jdbc.dao;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @description: 封装了针对数据表的通用操作（增删改查）
 */
public abstract class BaseDao<T> {
    
    private Class<T> clazz;
    
    // public BaseDao() {
    // }
    
    // 静态代码块只在类加载时运行一次
    // 这是非静态代码块，因为每一次查询的结果都是Book对象的，而不是Book类的
    {
        // 下面的代码也可以编写在上面的无参构造器中，先执行代码块，再执行构造器
        // 获取当前子类继承的带泛型的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();// 获取父类的泛型参数
        clazz = (Class<T>) actualTypeArguments[0];// 泛型的第一个参数
    }
    
    private QueryRunner queryRunner = new QueryRunner();
    
    // 增删改（3.0）：考虑上事务
    public void update(Connection connection, String sql, Object... args) {
        // SQL中占位符的个数与可变形参的长度一致
        
        // ·不主动获取connection连接
        
        try {
            queryRunner.update(connection, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // ·不主动关闭connection连接
    }
    
    
    // 通用的针对不同表的查询操作，返回数据库表中多条记录构成的集合（3.0）：考虑上事务
    public List<T> query(Connection connection, String sql, Object... args) {
        try {
            return queryRunner.query(connection, sql, new BeanListHandler<T>(clazz), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /// 通用的查询特殊值操作，例如：查询最大值、最近年份等
    public <E> E getValue(Connection connection, String sql, Object... args) {
        try {
            return queryRunner.query(connection, sql, new ScalarHandler<>(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}