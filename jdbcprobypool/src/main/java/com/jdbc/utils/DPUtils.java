package com.jdbc.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: 数据库连接池工具
 */
public class DPUtils {
    
    /**
     * 创建C3P0数据库连接池
     */
    // 数据连接池只需要创建一个，防止每次连接每次创建
    // 无参数需要自定义配置或直接调用默认config
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();
    
    /**
     * 获取C3P0数据库连接池连接
     *
     * @return
     *
     * @throws SQLException
     */
    public static Connection getConnectionByC3P0() {
        Connection connection;
        try {
            connection = cpds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    
    
    /**
     * 创建DBCP数据库连接池
     */
    private static BasicDataSource basicDataSource = null;
    
    static {
        try {
            FileInputStream inputStream = new FileInputStream("src/main/resources/dbcp.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            
            basicDataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取DBCP数据库连接池连接
     *
     * @return
     *
     * @throws Exception
     */
    public static Connection getConnectionByDBCP() {
        Connection connection;
        try {
            connection = basicDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    
    
    /**
     * 创建Druid数据库连接池
     */
    private static DataSource source = null;
    
    static {
        try {
            // FileInputStream inputStream = new FileInputStream("src/main/resources/druid.properties");
            
            // 使用当前类获取ClassLoader
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            
            Properties properties = new Properties();
            properties.load(inputStream);
            source = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Druid数据库连接池连接
     *
     * @return
     *
     * @throws Exception
     */
    public static Connection getConnectionByDruid() {
        Connection connection;
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    
    
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
    
    
    /**
     * 使用DBUtils工具类关闭资源
     *
     * @param resultSet
     * @param preparedStatement
     * @param connection
     */
    public static void closeResourceByDBUtils(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        // try {
        //     DbUtils.close(resultSet);
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        // try {
        //     DbUtils.close(preparedStatement);
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        // try {
        //     DbUtils.close(connection);
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        
        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(preparedStatement);
        DbUtils.closeQuietly(connection);
    }
}