import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: DBCP数据池测试
 */
public class DBCPTest {
    /**
     * 方式一：硬编码方式
     *
     * @throws SQLException
     */
    @Test
    public void connectTest() throws SQLException {
        // 创建DBCP数据库连接池
        BasicDataSource basicDataSource = new BasicDataSource();
        
        // 设置数据库需要的配置信息
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/book?");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("623363564");
        // 设置连接池的参数
        basicDataSource.setInitialSize(5);// 设置连接池初始化的连接数目
        
        Connection connection = basicDataSource.getConnection();
        System.out.println(connection);
    }
    
    
    /**
     * 方式二：配置文件方式
     *
     * @throws Exception
     */
    @Test
    public void connectByPropertiesTest() throws Exception {
        FileInputStream inputStream = new FileInputStream("src/main/resources/dbcp.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        
        // 创建DBCP数据库连接池
        BasicDataSource basicDataSource = BasicDataSourceFactory.createDataSource(properties);
        
        Connection connection = basicDataSource.getConnection();
        System.out.println(connection);
    }
}