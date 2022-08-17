import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: 连接测试
 */
public class ConnectionTest {

    @Test
    public void testConnection() throws ClassNotFoundException, SQLException {

        /*1、加载并注册数据库驱动*/
       //DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());//加载 并 注册
        Class.forName("com.mysql.cj.jdbc.Driver");//加载和注册都在这个方法执行

        /*2、配置信息*/
        String url = "jdbc:mysql://localhost:3306/book?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String psd = "623363564";

        /*3、获取数据库连接*/
        Connection connection = DriverManager.getConnection(url, user, psd);

        System.out.println(connection);
    }


    @Test
    public void getConnectionByProperties() throws IOException, ClassNotFoundException, SQLException {


        /*1、读取配置文件的信息*/
//        FileInputStream inputStream=new FileInputStream("jdbc.properties");//这样写会找不到指定文件
        /*
        * 注意存放路径，test 和 main 是两个不同的包，配置文件如果直接放在 test/java 里面就可以用上面的写法
        * 且 main 包下的 java 才是源根目录，里面的类才可以直接调用 resources 资源包里面的配置
        * */
        FileInputStream inputStream=new FileInputStream("jdbc.properties");

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
        System.out.println(connection);

    }


}
