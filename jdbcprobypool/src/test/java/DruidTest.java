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
 * @description: Druid 数据池测试
 */
public class DruidTest {
    
    @Test
    public void testDruid() throws SQLException {
        
        //创建Druid数据库连接池
        DruidDataSource druidDataSource = new DruidDataSource();
        
        //设置数据库需要的配置信息
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/book?");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("623363564");
        
        DruidPooledConnection connection = druidDataSource.getConnection();
        System.out.println(connection);
    }
    
    
    @Test
    public void testDruidByProperties() throws Exception {
        
        //FileInputStream inputStream = new FileInputStream("src/main/resources/druid.properties");
        
        //使用当前的类获取 classLoader
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        
        Properties properties = new Properties();
        properties.load(inputStream);
        
        //创建 Druid 数据库连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
    
}
