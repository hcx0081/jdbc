import com.jdbc.dao.impl.BookDaoImpl;
import com.jdbc.pojo.Book;
import com.jdbc.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * @description:
 */
class BookDaoImplTest {
    private BookDaoImpl bookDao = new BookDaoImpl();
    
    @Test
    void insert() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        Book book = new Book(10, "《计算机》", "未知", 110.0, 20);
        bookDao.insert(connection, book);
        System.out.println("添加成功");
        JDBCUtils.closeResource(null, connection);
    }
    
    @Test
    void deleteById() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        bookDao.deleteById(connection, "10");
        System.out.println("删除成功");
        JDBCUtils.closeResource(null, connection);
    }
    
    @Test
    void update() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        Book book = new Book(12435, "《计算机》", "未知", 110.0, 20);
        bookDao.update(connection, book);
        System.out.println("修改成功");
        JDBCUtils.closeResource(null, connection);
    }
    
    @Test
    void getAll() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Connection connection = JDBCUtils.getConnection();
        List<Book> allBooks = bookDao.getAll(connection);
        allBooks.forEach(System.out::println);
        JDBCUtils.closeResource(null, connection);
    }
    
    @Test
    void getCount() {
        Connection connection = JDBCUtils.getConnection();
        Long count = bookDao.getCount(connection);
        System.out.println("表中共有" + count + "条数据");
        JDBCUtils.closeResource(null, connection);
    }
    
    @Test
    void getMaxBookPrice() {
        Connection connection = JDBCUtils.getConnection();
        Double maxPrice = bookDao.getMaxPrice(connection);
        System.out.println("最高价格为" + maxPrice + "元");
        JDBCUtils.closeResource(null, connection);
    }
}