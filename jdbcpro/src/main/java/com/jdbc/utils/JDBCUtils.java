package com.jdbc.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @description: 数据库工具类
 */
public class JDBCUtils {
    /**
     * 连接数据库
     *
     * @return connection
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() {
        FileInputStream inputStream;
        Properties properties = new Properties();
        Connection connection;
        try {
            /* 1.读取配置文件的信息 */
            inputStream = new FileInputStream("src/main/resources/jdbc.properties");
            properties.load(inputStream);
            
            String driverClassName = properties.getProperty("driverClassName");
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String psd = properties.getProperty("psd");
            
            /* 2.加载驱动 */
            Class.forName(driverClassName);
            
            /* 3.获取数据库连接 */
            connection = DriverManager.getConnection(url, user, psd);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    
    
    /**
     * 关闭数据库增删改操作的资源
     *
     * @param connection
     * @param preparedStatement
     */
    public static void closeResource(PreparedStatement preparedStatement, Connection connection) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 关闭数据库查询操作的资源
     *
     * @param resultSet
     * @param preparedStatement
     * @param connection
     */
    public static void closeResource(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}