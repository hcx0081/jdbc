import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: Druid数据池测试
 */
public class DruidTest {
    /**
     * 方式一：硬编码方式
     *
     * @throws SQLException
     */
    @Test
    public void connectTest() throws SQLException {
        // 创建Druid数据库连接池
        DruidDataSource druidDataSource = new DruidDataSource();
        
        // 设置数据库需要的配置信息
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/book?");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("200081");
        
        DruidPooledConnection connection = druidDataSource.getConnection();
        System.out.println(connection);
    }
    
    
    /**
     * 方式二：配置文件方式
     *
     * @throws Exception
     */
    @Test
    public void connectByPropertiesTest() throws Exception {
        // FileInputStream inputStream = new FileInputStream("src/main/resources/druid.properties");
    
        // 使用当前类获取ClassLoader
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        
        Properties properties = new Properties();
        properties.load(inputStream);
        
        // 创建Druid数据库连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}