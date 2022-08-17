package com.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @description: 数据库连接工具类
 */
public class JDBCUtils {
    
    /**
     * 连接数据库
     *
     * @return connection
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        
        /*1、读取配置文件的信息*/
        //FileInputStream inputStream=new FileInputStream("jdbc.properties");//这样写会找不到指定文件
        /*
         * 注意存放路径，test 和 main 是两个不同的包，配置文件如果直接放在 test/java 里面就可以用上面的写法
         * 且 main 包下的 java 才是源根目录，里面的类才可以直接调用 resources 资源包里面的配置
         * */
        FileInputStream inputStream = new FileInputStream("src/main/resources/jdbc.properties");
        
        Properties properties = new Properties();
        
        properties.load(inputStream);
        
        String driverClassName = properties.getProperty("driverClassName");
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String psd = properties.getProperty("psd");
        
        /*2、加载驱动*/
        Class.forName(driverClassName);
        
        /*3、获取数据库连接*/
        Connection connection = DriverManager.getConnection(url, user, psd);
        
        return connection;
    }
    
    
    /**
     * 关闭数据库更新操作的资源
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
