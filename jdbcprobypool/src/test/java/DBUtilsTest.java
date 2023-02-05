import com.jdbc.utils.DPUtils;
import com.jdbc.pojo.Book;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @description: Dbutils测试
 */
public class DBUtilsTest {
    
    /**
     * 测试插入（更新、删除类似）
     */
    @Test
    public void insertTest() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "insert into book(id, name, author, price, num) values(?, ?, ?, ?, ?)";
            int insertCount = queryRunner.update(connectionByDruid, sql, "555", "《asp》", "zs", "110", "20");
            System.out.println("添加了" + insertCount + "条数据");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    @Test
    public void queryByBeanTest() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select id, name, author, price, num from book where author = ?";
            
            List<Book> bookList = queryRunner.query(connectionByDruid, sql, new BeanListHandler<>(Book.class), "王小波");
            bookList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    @Test
    public void queryByMapTest() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select id, name, author, price, num from book where author = ?";
            
            List<Map<String, Object>> mapList = queryRunner.query(connectionByDruid, sql, new MapListHandler(), "王小波");
            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    @Test
    public void queryByScalarTest() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select count(*) from book";
            
            long count = (long) queryRunner.query(connectionByDruid, sql, new ScalarHandler<>());
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    /**
     * 自定义ResultSetHandler的实现类
     */
    @Test
    public void queryByCustomTest() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select id, name, author, price, num from book where author = ?";
            
            ResultSetHandler<Book> resultSetHandler = new ResultSetHandler<Book>() {
                @Override
                public Book handle(ResultSet rs) throws SQLException {
                    // System.out.println("handle");
                    // return null;
                    
                    if (rs.next()) {
                        Integer id = rs.getInt("id");
                        String name = rs.getString("name");
                        String author = rs.getString("author");
                        double price = rs.getDouble("price");
                        int num = rs.getInt("num");
                        Book book = new Book(id, name, author, price, num);
                        return book;
                    }
                    return null;
                }
            };
            
            Book book = queryRunner.query(connectionByDruid, sql, resultSetHandler, "王小波");
            System.out.println(book);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
}