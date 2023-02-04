import com.jdbc.pojo.Book;
import com.jdbc.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 查询测试类
 */
public class PreparedStatementQueryTest {
    // 查
    @Test
    public void queryTest() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        
        String sql = "select id, name, author, price, num from book where author = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 填充占位符
        preparedStatement.setString(1, "王小波");
        
        // 执行SQL语句，并返回结果集
        ResultSet resultSet = preparedStatement.executeQuery();
        // 迭代结果集
        while (resultSet.next()) {// 判断结果集的下一条是否有记录，如果有记录返回true，指针下移；如果没有记录返回false，指针不会下移
            /* 获取当前记录的各个字段值 */
            // 方法一
            // Integer id = resultSet.getInt(1);
            // String name = resultSet.getString(2);
            // String author = resultSet.getString(3);
            // double price = resultSet.getDouble(4);
            // int num = resultSet.getInt(5);
            
            // 方法二：可读性强，不会被字段顺序所限制
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            double price = resultSet.getDouble("price");
            int num = resultSet.getInt("num");
            
            
            // 方式一
            // System.out.println("编号=" + id + ",书名=" + name + ",作者=" + author + ",价格=" + price + ",库存=" + num);
            
            // 方式二：封装成数组，遍历输出
            // Object[] result = new Object[]{id, name, author, price, num};
            
            // 方式三：将数据封装成一个对象
            Book book = new Book(id, name, author, price, num);
            System.out.println(book);
        }
        JDBCUtils.closeResource(resultSet, preparedStatement, connection);
    }
    
    
    // 通用的针对不同表的查询操作
    public <T> List<T> query(Class<T> clazz, String sql, String... args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        // 填充占位符
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);// 注意参数
        }
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        // 获取结果集的元数据
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 通过ResultSetMetaData获取结果集的列数（即有几个属性）
        int columnCount = metaData.getColumnCount();
        
        // 创建存储记录的集合
        ArrayList<T> list = new ArrayList<>();
        while (resultSet.next()) {// 判断结果集的下一条是否有记录，如果有记录返回true，指针下移；如果没有记录返回false，指针不会下移
            T t = clazz.newInstance();// 创建一个指定表的映射实体对象
            // 处理每一条记录中的每一列（即属性），给t对象的指定属性赋值
            for (int i = 0; i < columnCount; i++) {
                // 获取每一列的值
                Object columnValue = resultSet.getObject(i + 1);
                
                // 获取每一列的列名：getColumnName() 不推荐使用
                // 获取每一列的别名（没有别名则获取列名）：getColumnLabel() 防止实体类的属性和数据库的字段名称不匹配，编写SQL语句时建议给每一个字段加上别名
                String columnName = metaData.getColumnLabel(i + 1);
                
                // 通过反射给t对象的指定columnName属性赋值为columnValue
                Field declaredField = t.getClass().getDeclaredField(columnName);
                declaredField.setAccessible(true);// 属性可能是private，所以设置为true禁用安全检查
                declaredField.set(t, columnValue);
            }
            list.add(t);
        }
        JDBCUtils.closeResource(resultSet, preparedStatement, connection);
        return list;
    }
    
    
    @Test
    public void commonQueryTest() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String sql = "select id, name, author, price, num from book where author = ?";
        List<Book> bookList = query(Book.class, sql, "王小波");
        // for (Book book : bookList) {
        //    System.out.println(book);
        // }
        bookList.forEach(System.out::println);
    }
}