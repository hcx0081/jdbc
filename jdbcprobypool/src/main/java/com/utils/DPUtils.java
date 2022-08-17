package com.utils;

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
 * @description: 数据库连接池连接工具
 */
public class DPUtils {
    
    /**
     * 创建 C3P0 数据库连接池
     */
    //数据连接池只需要造一个，防止每次连接每次造
    //无参数需要自定义配置或直接调用默认的 config
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();
    /**
     * 获取 C3P0 数据库连接池连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnectionByC3P0() throws SQLException {
        Connection connection = cpds.getConnection();
        return connection;
    }




    /**
     * 创建 DBCP 数据库连接池
     */
    private static  BasicDataSource basicDataSource =null;
    static {
        try {
            FileInputStream inputStream=new FileInputStream("src/main/resources/dbcp.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            basicDataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取 DBCP 数据库连接池连接
     * @return
     * @throws Exception
     */
    public static Connection getConnectionByDBCP() throws Exception {
        Connection connection = basicDataSource.getConnection();
        return connection;
    }




    /**
     * 创建 Druid 数据库连接池
     */
    private static DataSource source=null;
    static {
        try {
            //FileInputStream inputStream = new FileInputStream("src/main/resources/druid.properties");

            //使用当前的类获取 classLoader
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

            Properties properties = new Properties();
            properties.load(inputStream);
            source = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取 Druid 数据库连接池连接
     * @return
     * @throws Exception
     */
    public static Connection getConnectionByDruid() throws Exception {
        Connection connection = source.getConnection();
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
     * 使用 DBUtils 工具类关闭资源
     * @param resultSet
     * @param preparedStatement
     * @param connection
     */
    public static void closeResourceByDBUtils(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        /*try {
            DbUtils.close(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DbUtils.close(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DbUtils.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(preparedStatement);
        DbUtils.closeQuietly(connection);

    }
}
