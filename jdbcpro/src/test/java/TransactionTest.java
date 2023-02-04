import com.jdbc.pojo.User;
import com.jdbc.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 事务测试
 */
public class TransactionTest {
    // 增删改（2.0）：考虑上事务
    public void update(Connection connection, String sql, Object... args) throws SQLException {
        // SQL 中占位符的个数与可变形参的长度一致
        
        // ·不主动获取connection连接
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);// 注意参数
        }
        preparedStatement.execute();
        
        // ·不主动关闭connection
        JDBCUtils.closeResource(preparedStatement, null);
    }
    
    
    /**
     * 针对数据表user
     * A给B转账1000
     * <p>
     * BEGIN TRANSACTION
     * <p>
     * A账户减少1000元
     * <p>
     * B账户增加1000元
     * <p>
     * COMMIT
     */
    @Test
    public void commonUpdateByTransactionTest() {
        Connection connection = null;
        try {
            // ·主动获取connection连接
            connection = JDBCUtils.getConnection();
            
            // ·取消事务的自动提交，默认为true，即自动提交
            connection.setAutoCommit(false);
            
            String sql1 = "update user set balance = balance-1000 where name = ?";
            update(connection, sql1, "A");
            // 模拟网络异常，如果没有事务，则A的账户会减少1000元，但是B的账户不会增加
            // System.out.println(10/0);
            String sql2 = "update user set balance=balance+1000 where name = ?";
            update(connection, sql2, "B");
            System.out.println("转账成功");
            
            // ·提交数据
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // ·有异常则回滚数据
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                // ·开启事务的自动提交（以前什么样，还回去就要什么样），主要针对数据库连接池的使用
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // ·主动关闭connection连接
            JDBCUtils.closeResource(null, connection);
        }
    }
    
    
    // 通用的针对不同表的查询操作（2.0）：考虑上事务
    public <T> List<T> query(Connection connection, Class<T> clazz, String sql, String... args) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
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
    
    
    
    /*
     * 模拟有网络延迟的存款，
     * 在网络延迟期间查询账户金额应该是不变的
     * */
    
    @Test
    public void commonQueryByTransactionTest() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Connection connection = JDBCUtils.getConnection();
        
        // 获取当前连接的隔离级别
        System.out.println(connection.getTransactionIsolation());// 默认为4：TRANSACTION_REPEATABLE_READ
        
        // 设置数据库的隔离级别为读已提交，避免脏读
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        System.out.println(connection.getTransactionIsolation());// 2
        
        
        String sql = "select name, balance from user where name = ?";
        List<User> userList = query(connection, User.class, sql, "A");
        System.out.println(userList);
    }
    
    @Test
    public void commonUpdate2ByTransactionTest() throws SQLException, InterruptedException {
        Connection connection = JDBCUtils.getConnection();
        // 取消事务的自动提交，默认为true，即自动提交
        connection.setAutoCommit(false);
        
        String sql = "update user set balance=balance+? where name = ?";
        update(connection, sql, 1000, "A");
        
        // 模拟网络延迟
        Thread.sleep(15000);
        // 网络延迟后提交事务
        connection.commit();
        
        // 开启事务的自动提交，主要针对数据库连接池
        connection.setAutoCommit(true);
        
        System.out.println("存款成功");
    }
}