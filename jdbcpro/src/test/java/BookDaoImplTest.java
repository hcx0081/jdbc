import com.dao.impl.BookDaoImpl;
import com.utils.JDBCUtils;
import com.pojo.Book;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * @description:
 */
class BookDaoImplTest {
    
    private BookDaoImpl bookDao = new BookDaoImpl();
    
    @Test
    void insert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Book book = new Book("10", "《计算机》", "未知", 110.0, 20);
            bookDao.insert(connection, book);
            System.out.println("添加成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, connection);
        }
    }
    
    @Test
    void deleteByBookid() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            bookDao.deleteByBookid(connection, "10");
            System.out.println("删除成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, connection);
        }
    }
    
    @Test
    void update() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Book book = new Book("12434", "《计算机》", "未知", 110.0, 20);
            bookDao.update(connection, book);
            System.out.println("修改成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, connection);
        }
    }
    
    @Test
    void getAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            List<Book> allBooks = bookDao.getAll(connection);
            allBooks.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, connection);
        }
    }
    
    @Test
    void getCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Long count = bookDao.getCount(connection);
            System.out.println("表中共有" + count + "条数据");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, connection);
        }
    }
    
    @Test
    void getMaxBookprice() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Double maxBookprice = bookDao.getMaxBookprice(connection);
            System.out.println("表中最高价格为" + maxBookprice + "元");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, connection);
        }
    }
}