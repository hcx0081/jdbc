import com.utils.DPUtils;
import com.pojo.Book;
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
 * @description: dbutils 测试
 */
public class DBUtilsTest {
    
    /**
     * 测试插入（更新、删除类似）
     */
    @Test
    public void testInsert() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "insert into book_info(bookid,bookname,bookautor,bookprice,booknum) values(?,?,?,?,?)";
            int insertCount = queryRunner.update(connectionByDruid, sql, "555", "《asp》", "zhangsan", "110", "20");
            System.out.println("添加了" + insertCount + "条数据");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    @Test
    public void testQueryByBean() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select bookid,bookname,bookautor,bookprice,booknum from book_info where bookautor= ?";
            
            //BeanHandler 是 ResultSetHandler 接口的实现类，用于封装表中的一条数据
            //BeanListHandler 是 ResultSetHandler 接口的实现类，用于封装表中的多条数据
            List<Book> books = queryRunner.query(connectionByDruid, sql, new BeanListHandler<>(Book.class), "5");
            books.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    @Test
    public void testQueryByMap() {
        
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select bookid,bookname,bookautor,bookprice,booknum from book_info where bookautor= ?";
            
            //MapHandler 是 ResultSetHandler 接口的实现类，用于封装表中的一条数据
            //MapListHandler 是 ResultSetHandler 接口的实现类，用于封装表中的多条数据
            List<Map<String, Object>> maps = queryRunner.query(connectionByDruid, sql, new MapListHandler(), "5");
            maps.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    @Test
    public void testQueryByScalar() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select count(*) from book_info";
            
            //将 ResultSet 中的某一条记录的其中某一列的数据储存成 Object 对象，用于封装表中的一条数据
            long count = (long) queryRunner.query(connectionByDruid, sql, new ScalarHandler<>());
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
    
    
    /**
     * 自定义 ResultSetHandler 的实现类
     */
    @Test
    public void testQueryByCustom() {
        Connection connectionByDruid = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connectionByDruid = DPUtils.getConnectionByDruid();
            String sql = "select bookid,bookname,bookautor,bookprice,booknum from book_info where bookautor= ?";
            
            ResultSetHandler<Book> resultSetHandler = new ResultSetHandler<Book>() {
                @Override
                public Book handle(ResultSet rs) throws SQLException {
                    //System.out.println("handle");
                    //return null;
                    
                    if (rs.next()) {
                        String bookid = rs.getString("bookid");
                        String bookname = rs.getString("bookname");
                        String bookautor = rs.getString("bookautor");
                        double bookprice = rs.getDouble("bookprice");
                        int booknum = rs.getInt("booknum");
                        Book book = new Book(bookid, bookname, bookautor, bookprice, booknum);
                        return book;
                    }
                    return null;
                    
                }
            };
            
            Book book = queryRunner.query(connectionByDruid, sql, resultSetHandler, "5");
            System.out.println(book);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DPUtils.closeResource(null, null, connectionByDruid);
        }
    }
}
