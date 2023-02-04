import com.jdbc.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: 增删改测试类
 */
public class PreparedStatementUpdateTest {
    // 增
    @Test
    public void insertTest() throws IOException, SQLException, ClassNotFoundException {
        /* 1.读取配置文件的信息 */
        // FileInputStream inputStream=new FileInputStream("jdbc.properties");// 测试资源目录中并没有该文件
        FileInputStream inputStream = new FileInputStream("src/main/resources/jdbc.properties");
        Properties properties = new Properties();
        
        properties.load(inputStream);
        
        String driverClassName = properties.getProperty("driverClassName");
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String psd = properties.getProperty("psd");
        
        /* 2.加载驱动 */
        Class.forName(driverClassName);
        
        /* 3.获取数据库连接 */
        Connection connection = DriverManager.getConnection(url, user, psd);
        
        /* 4.预编译SQL语句，返回preparedStatement对象 */
        String sql = "insert into book(id, name, author, price, num) values(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        /* 5.填充占位符（即?，第1个占位符的索引为1，第2个为2，以此类推） */
        preparedStatement.setString(1, "12345");
        preparedStatement.setString(2, "《爱你就像爱生命》");
        preparedStatement.setString(3, "王小波");
        preparedStatement.setDouble(4, 20);
        preparedStatement.setInt(5, 1314);
        
        /* 6.执行操作 */
        preparedStatement.execute();
        
        /* 7.关闭资源，避免出现内存泄漏 */
        preparedStatement.close();
        connection.close();
    }
    
    
    // 改
    @Test
    public void updateTest() throws SQLException {
        /* 1.获取数据库连接 */
        Connection connection = JDBCUtils.getConnection();
        
        /* 2.预编译SQL语句，返回preparedStatement对象 */
        String sql = "update book set name = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        /* 5.填充占位符（即?，第1个占位符的索引为1，第2个为2，以此类推） */
        preparedStatement.setString(1, "C++");
        preparedStatement.setObject(2, "1");// 也可以使用setObject替代setString，因为Object是String的父类
        
        /* 6.执行操作 */
        preparedStatement.execute();
        
        /* 7.关闭资源，避免出现内存泄漏 */
        JDBCUtils.closeResource(preparedStatement, connection);
    }
    
    
    // 通用的增删改操作
    public void update(String sql, Object... args) throws SQLException {
        // SQL中占位符的个数与可变形参的长度一致
        
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        // 填充占位符
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);// 注意参数
        }
        preparedStatement.execute();
        JDBCUtils.closeResource(preparedStatement, connection);
    }
    
    
    // 增删改，通用的更新操作
    @Test
    public void commonUpdateTest() throws SQLException {
        // String sql = "delete from book where id = ?";
        // update(sql, "1");
        
        String sql = "insert into book(id, name, author, price, num) values(?, ?, ?, ?, ?)";
        update(sql, "222", "《杀死一只知更鸟》", "未知", 220, 5);
    }
}