package com.jdbc.dao;

import com.jdbc.utils.JDBCUtils;

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
    
    // 增删改（2.0）：考虑上事务
    public void update(Connection connection, String sql, Object... args) throws SQLException {
        // SQL中占位符的个数与可变形参的长度一致
        
        // ·不主动获取connection连接
        
        PreparedStatement preparedStatement = null;
        
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);// 注意参数
        }
        preparedStatement.execute();
        
        // ·不主动关闭connection连接
        JDBCUtils.closeResource(preparedStatement, null);
    }
    
    
    // 通用的针对不同表的查询操作，返回数据库表中多条记录构成的集合（2.0）：考虑上事务
    public List<T> query(Connection connection, String sql, Object... args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        // 获取结果集的元数据
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 通过ResultSetMetaData获取结果集的列数（即有几个属性）
        int columnCount = metaData.getColumnCount();
        
        // 创建存储记录的集合
        ArrayList<T> list = new ArrayList<>();
        while (resultSet.next()) {// 判断结果集的下一条是否有记录，如果有记录返回true，指针下移；如果没有记录返回false，指针不会下移
            T t = clazz.newInstance();// 创建一个指定表的映射实体对象
            // 处理每一条记录中的每一列（即属性），给t对象的指定属性赋值
            for (int i = 0; i < columnCount; i++) {
                // 获取每一列的值
                Object columnValue = resultSet.getObject(i + 1);
                
                // 获取每一列的列名：getColumnName() 不推荐使用
                // 获取每一列的别名（没有别名则获取列名）：getColumnLabel() 防止实体类的属性和数据库的字段名称不匹配，编写SQL语句时建议给每一个字段加上别名
                String columnName = metaData.getColumnLabel(i + 1);
                
                // 通过反射给t对象的指定columnName属性赋值为columnValue
                Field declaredField = t.getClass().getDeclaredField(columnName);
                declaredField.setAccessible(true);// 属性可能是private，所以设置为true禁用安全检查
                declaredField.set(t, columnValue);
            }
            list.add(t);
        }
        JDBCUtils.closeResource(resultSet, preparedStatement, null);
        return list;
    }
    
    
    // 通用的查询特殊值操作，例如：查询最大值、最近年份等
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