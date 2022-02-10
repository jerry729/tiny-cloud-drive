package util;


import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.sql.*;
import java.util.Properties;

/**
 * @author :frank
 * @date :20:19 2020/12/12
 * @description :TODO
 */
public class JDBCUtils {

    private static String url;
    private static String user;
    private static String password;
    private static String driver;


    static {
        try {
            Properties properties = new Properties();
            InputStream in = JDBCUtils.class.getClassLoader()
                    .getResourceAsStream("util/jdbc.properties");
            properties.load(in);

            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            driver = properties.getProperty("driveName");

            Class.forName(driver);

        } catch (IOException exception) {
            exception.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection init() throws SQLException {
        Connection connection = DriverManager.getConnection(url,user,password);
        return connection;
    }

    public static boolean disconnectFromDataBase(ResultSet resultSet,
                                                 PreparedStatement preparedStatement,
                                                 Connection connection){

        try {
            if (resultSet != null){
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }finally {
                try {
                    if (connection != null){
                        connection.close();
                        connection = null;
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }finally {
                    return true;
                }
            }
        }
    }

    public static boolean disconnectFromDataBase(PreparedStatement preparedStatement,
                                                 Connection connection){
        try {
            if (preparedStatement != null){
                preparedStatement.close();
                preparedStatement = null;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            try {
                if (connection != null){
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }finally {
                return true;
            }
        }
    }
}


