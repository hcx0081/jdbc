import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: 连接测试
 */
public class ConnectionTest {
    @Test
    public void getConnection() throws ClassNotFoundException, SQLException {
        /* 1.加载并注册数据库驱动 */
        // DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());// 加载 并 注册
        Class.forName("com.mysql.cj.jdbc.Driver");// 加载和注册使用这个方法执行
        
        /* 2.配置信息 */
        String url = "jdbc:mysql://localhost:3306/book?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String psd = "200081";
        
        /* 3.获取数据库连接 */
        Connection connection = DriverManager.getConnection(url, user, psd);
        
        System.out.println(connection);
    }
    
    
    @Test
    public void getConnectionByProperties() throws IOException, ClassNotFoundException, SQLException {
        /* 1.读取配置文件的信息 */
        // FileInputStream inputStream=new FileInputStream("jdbc.properties");// 测试资源目录中并没有该文件
        FileInputStream inputStream = new FileInputStream("src/main/resources/jdbc.properties");
        
        Properties properties = new Properties();
        
        // 加载配置信息
        properties.load(inputStream);
        
        String driverClassName = properties.getProperty("driverClassName");
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String psd = properties.getProperty("psd");
        
        /* 2.加载驱动 */
        Class.forName(driverClassName);
        
        /* 3.获取数据库连接 */
        Connection connection = DriverManager.getConnection(url, user, psd);
        System.out.println(connection);
    }
}