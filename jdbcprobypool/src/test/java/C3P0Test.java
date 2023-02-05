import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description: c3p0数据池测试
 */
public class C3P0Test {
    /**
     * 方式一：硬编码方式连接数据池
     *
     * @throws Exception
     */
    @Test
    public void connectTest() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.cj.jdbc.Driver");// loads the jdbc driver
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/book");
        cpds.setUser("root");
        cpds.setPassword("200081");
        
        /* 通过设置相关的参数对数据库连接池进行管理 */
        
        // 设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(5);
        
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
    
    
    /**
     * 方式二：配置文件方式连接数据池
     *
     * @throws SQLException
     */
    @Test
    public void connectByXmlTest() throws SQLException {
        // 无参数需要自定义配置或直接调用默认的config
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
}