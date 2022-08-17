import com.utils.JDBCUtils;
import com.pojo.Book;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 查询测试类
 */
public class PreparedStatementQueryTest {
    
    //查
    @Test
    public void testQuery() {
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = JDBCUtils.getConnection();
            
            String sql = "select bookid,bookname,bookautor,bookprice,booknum from book_info where bookautor =?";
            
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            preparedStatement.setString(1, "5");
            
            //执行，并返回结果集
            resultSet = preparedStatement.executeQuery();
            //处理结果集
            while (resultSet.next()) {  //判断结果集的下一条是否有数据，如果有数据返回true，并指针下移；如果为false，则不会下移
                
                /*获取当前字段的各个字段值*/
                //方法一
                //String bookid = resultSet.getString(1);
                //String bookname = resultSet.getString(2);
                //String bookautor = resultSet.getString(3);
                //double bookprice = resultSet.getDouble(4);
                //int booknum = resultSet.getInt(5);
                
                //方法二：可读性强，不会被字段顺序所限制
                String bookid = resultSet.getString("bookid");
                String bookname = resultSet.getString("bookname");
                String bookautor = resultSet.getString("bookautor");
                double bookprice = resultSet.getDouble("bookprice");
                int booknum = resultSet.getInt("booknum");
                
                
                //方式一
                //System.out.println("编号=" + bookid + ",书名=" + bookname + ",作者=" + bookautor + ",价格=" + bookprice + ",库存=" + booknum);
                
                //方式二:封装成数组,遍历输出
                //Object[] result = new Object[]{bookid, bookname, bookautor, bookprice, booknum};
                
                //方式三:将数据封装成一个对象
                Book book = new Book(bookid, bookname, bookautor, bookprice, booknum);
                System.out.println(book);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(resultSet, preparedStatement, connection);
        }
    }
    
    
    //通用的对不同表的查询操作
    public <T> List<T> query(Class<T> clazz, String sql, String... args) {
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = JDBCUtils.getConnection();
            
            preparedStatement = connection.prepareStatement(sql);
            
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            
            resultSet = preparedStatement.executeQuery();
            
            //获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过 ResultSetMetaData 获取结果集的列数（即有几个属性）
            int columnCount = metaData.getColumnCount();
            
            //创建对象
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {  //判断结果集的下一条是否有数据，如果有数据返回 true，并指针下移；如果为 false，则不会下移
                T t = clazz.newInstance();//有结果集再创建一个对象
                //处理一行数据中的每一个列（即属性），给 t 对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = resultSet.getObject(i + 1);
                    
                    //获取每个列的列名 getColumnName()  --不推荐使用
                    //获取每个列的别名（没有别名则获取列名） getColumnLabel() 防止实体类的属性和数据库的字段名称不匹配，写 SQL 语句时给每个字段加上别名
                    String columnName = metaData.getColumnLabel(i + 1);
                    
                    //给 t 对象指定的 columnName 属性赋值为 columnValue，通过反射
                    Field declaredField = t.getClass().getDeclaredField(columnName);
                    declaredField.setAccessible(true);//属性可能是 private，所以设置为 true，可见
                    declaredField.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(resultSet, preparedStatement, connection);
        }
        
        //如果没找到结果集
        return null;
    }
    
    
    @Test
    public void testQueryCommon() {
        
        String sql = "select bookid,bookname,bookautor,bookprice,booknum from book_info where bookautor= ?";
        List<Book> bookList = query(Book.class, sql, "5");
        //for (Book book : bookList) {
        //    System.out.println(book);
        //}
        bookList.forEach(System.out::println);
        
    }
    
    
}
