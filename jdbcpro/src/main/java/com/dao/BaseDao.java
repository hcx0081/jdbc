package com.dao;

import com.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 封装了针对数据表的通用操作（增删改查）
 */
public abstract class BaseDao<T> {
    
    private Class<T> clazz;
    
    //public BaseDao() {
    //}
    
    //静态代码块只在类加载时运行一次
    //这是非静态代码块，因为每次查询的结果都是对象的，而不是整个Book的
    {
        //下面代码写在上面的无参构造器中也可以，代码块先执行，再执行构造器
        //获取当前子类继承的带泛型的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取父类的泛型参数
        clazz = (Class<T>) actualTypeArguments[0];//泛型的第一个参数
    }
    
    //增删改(2.0)--考虑上事务
    public void update(Connection connection, String sql, Object... args) {
        // SQL 中占位符的个数与可变形参的长度一致
        
        
        //·不主动获取connection连接
        
        PreparedStatement preparedStatement = null;
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);//注意参数
            }
            preparedStatement.execute();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //·不主动关闭 connection
            JDBCUtils.closeResource(preparedStatement, null);
        }
    }
    
    
    //通用的对不同表的查询操作，返回数据表中多条记录构成的集合(2.0)--考虑上事务
    public List<T> query(Connection connection, String sql, Object... args) {
        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            
            resultSet = preparedStatement.executeQuery();
            
            //获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过ResultSetMetaData获取结果集的列数（即有几个属性）
            int columnCount = metaData.getColumnCount();
            
            //创建对象
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {  //判断结果集的下一条是否有数据，如果有数据返回true，并指针下移；如果为false，则不会下移
                T t = clazz.newInstance();//有结果集再创建一个对象
                //处理一行数据中的每一个列（即属性），给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = resultSet.getObject(i + 1);
                    
                    //getColumnName()获取每个列的列名--不推荐使用
                    //getColumnLabel()获取每个列的别名（没有别名则获取列名）,防止实体类的属性和数据库的字段名称不匹配,写SQL语句时给每个字段加上别名
                    String columnName = metaData.getColumnLabel(i + 1);
                    
                    //给t对象指定的columnName属性赋值为columnValue，通过反射
                    Field declaredField = t.getClass().getDeclaredField(columnName);
                    declaredField.setAccessible(true);//属性可能是 private，所以设置为 true，可见
                    declaredField.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
            
        } catch (SQLException | NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(resultSet, preparedStatement, null);
        }
        
        //如果没找到结果集
        return null;
    }
    
    
    //用于查询特殊值的通用方法，如查询最大值，最近年份等
    public <E> E getValue(Connection connection, String sql, Object... args) {
        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(resultSet, preparedStatement, null);
        }
        return null;
    }
}
