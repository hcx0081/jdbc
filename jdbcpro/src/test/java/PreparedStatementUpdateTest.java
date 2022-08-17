import com.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: 更新测试类
 */
public class PreparedStatementUpdateTest {
    
    //增
    @Test
    public void testInsert() {
        
        FileInputStream inputStream = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        
        try {
            
            /*1、读取配置文件的信息*/
//        FileInputStream inputStream=new FileInputStream("jdbc.properties");//这样写会找不到指定文件
            /*
             * 注意存放路径，test 和 main 是两个不同的包，配置文件如果直接放在 test/java 里面就可以用上面的写法
             * 且 main 包下的 java 才是源根目录，里面的类才可以直接调用 resources 资源包里面的配置
             * */
            inputStream = new FileInputStream("src/main/resources/jdbc.properties");
            Properties properties = new Properties();
            
            properties.load(inputStream);
            
            String driverClassName = properties.getProperty("driverClassName");
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String psd = properties.getProperty("psd");
            
            /*2、加载驱动*/
            Class.forName(driverClassName);
            
            /*3、获取数据库连接*/
            connection = DriverManager.getConnection(url, user, psd);
            
            
            /*4、预编译 SQL 语句，返回 preparedStatement 的实例*/
            String sql = "insert into book_info(bookid,bookname,bookautor,bookprice,booknum) values(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            
            /*5、填充占位符（即 ?，第 1 个占位符的索引为 1，第 2 个为 2，以此类推）*/
            preparedStatement.setString(1, "12345");
            preparedStatement.setString(2, "《爱你就像爱生命》");
            preparedStatement.setString(3, "王小波");
            preparedStatement.setDouble(4, 20);
            preparedStatement.setInt(5, 1314);
            
            /*6、执行操作*/
            preparedStatement.execute();
            
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            /*7、资源关闭，避免出现内存泄漏*/
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
    
    
    //改
    @Test
    public void testUpdate() {
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            
            /*1、获取数据库连接*/
            connection = JDBCUtils.getConnection();
            
            /*2、预编译 SQL 语句，返回 preparedStatement 的实例*/
            String sql = "update book_info set bookname= ? where bookid=?";
            preparedStatement = connection.prepareStatement(sql);
            
            /*5、填充占位符（即 ?，第 1 个占位符的索引为 1，第 2 个为 2，以此类推）*/
            preparedStatement.setString(1, "C++");
            preparedStatement.setObject(2, "1");//也可以使用setObject代替setString，因为Object是String的父类
            
            /*6、执行操作*/
            preparedStatement.execute();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            /*7、资源关闭，避免出现内存泄漏*/
            JDBCUtils.closeResource(preparedStatement, connection);
        }
    }
    
    
    //增删改，通用的更新操作
    public void update(String sql, Object... args) {
        // SQL 中占位符的个数与可变形参的长度一致
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            
            connection = JDBCUtils.getConnection();
            
            preparedStatement = connection.prepareStatement(sql);
            
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);//注意参数
            }
            preparedStatement.execute();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(preparedStatement, connection);
        }
    }
    
    
    //增删改，通用的更新操作
    @Test
    public void testUpdateCommon() {
        
        //String sql = "delete from book_info where bookid=?";
        //update(sql, "1");
        
        String sql = "insert into book_info(bookid,bookname,bookautor,bookprice,booknum) values(?,?,?,?,?)";
        update(sql, "222", "《杀死一只知更鸟》", "未知", 220, 5);
        
    }
    
    
}
