import com.utils.JDBCUtils;
import com.pojo.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 事务测试
 */
public class TransactionTest {
    
    
    //增删改(2.0)--考虑上事务
    public void update(Connection connection, String sql, Object... args) {
        // SQL 中占位符的个数与可变形参的长度一致
        
        //·不主动获取connection连接
        
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);//注意参数
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //·不主动关闭connection
            JDBCUtils.closeResource(preparedStatement, null);
        }
    }
    
    
    /**
     * 针对数据表 user
     * A 给 B 转账 1000
     * <p>
     * BEGIN TRANSACTION
     * A 账户减少 1000 元
     * B 账户增加 1000 元
     * COMMIT
     */
    @Test
    public void testUpdateByTransaction() {
        
        Connection connection = null;
        try {
            //· 主动获取连接
            connection = JDBCUtils.getConnection();
            
            //· 取消数据的自动提交，默认情况是 true，即自动提交
            connection.setAutoCommit(false);
            
            String sql1 = "update user set userbalance=userbalance-1000 where username=?";
            update(connection, sql1, "A");
            //模拟网络异常，如果中间网络出现异常，则 A 的账户会少 1000 元，但是 B 不会增加
            //System.out.println(10/0);
            String sql2 = "update user set userbalance=userbalance+1000 where username=?";
            update(connection, sql2, "B");
            System.out.println("转账成功");
            
            //· 提交数据
            connection.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //· 有异常则回滚数据
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                //· 开启数据的自动提交（以前什么样，还回去就要什么样），主要针对数据库连接池的使用
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //· 主动关闭 connection
            JDBCUtils.closeResource(null, connection);
        }
    }
    
    
    //**********************************************************************
    
    //通用的对不同表的查询操作(2.0)--考虑上事务
    public <T> List<T> query(Connection connection, Class<T> clazz, String sql, String... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            
            preparedStatement = connection.prepareStatement(sql);
            
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
                    
                    //getColumnName()获取每个列的列名--不推荐使用
                    //getColumnLabel()获取每个列的别名（没有别名则获取列名）,防止实体类的属性和数据库的字段名称不匹配,写SQL语句时给每个字段加上别名
                    String columnName = metaData.getColumnLabel(i + 1);
                    
                    //给t对象指定的columnName属性赋值为columnValue,通过反射
                    Field declaredField = t.getClass().getDeclaredField(columnName);
                    declaredField.setAccessible(true);//属性可能是 private，所以设置为 true，可见
                    declaredField.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (SQLException | NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(resultSet, preparedStatement, null);
        }
        
        //如果没找到结果集
        return null;
    }
    
    
    
    /*
     * 模拟有网络延迟的存款,
     * 在网络延迟期间查询账户金额应该是不变的
     * */
    
    @Test
    public void testQueryByTransaction() throws SQLException, IOException, ClassNotFoundException {
        
        Connection connection = JDBCUtils.getConnection();
        
        //获取当前连接的隔离级别
        System.out.println(connection.getTransactionIsolation());//默认为4：TRANSACTION_REPEATABLE_READ
        
        //设置数据库的隔离级别为读已提交，避免脏读
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        System.out.println(connection.getTransactionIsolation());//2
        
        
        String sql = "select username,balance from user where username=?";
        List<User> users = query(connection, User.class, sql, "A");
        System.out.println(users);
    }
    
    @Test
    public void testUpdate2ByTransaction() throws SQLException, IOException, ClassNotFoundException, InterruptedException {
        
        Connection connection = JDBCUtils.getConnection();
        
        //取消数据的自动提交，默认情况是true，即自动提交
        connection.setAutoCommit(false);
        
        String sql = "update user set balance = balance + ? where username = ?";
        update(connection, sql, 1000, "A");
        
        //模拟网络延迟
        Thread.sleep(15000);
        //网络延迟后提交事务
        connection.commit();
        
        //开启数据的自动提交,主要针对数据库连接池
        connection.setAutoCommit(true);
        
        System.out.println("存款成功");
    }
}
