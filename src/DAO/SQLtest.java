package DAO;


import java.sql.*;


/**
 * @author :frank
 * @date :9:19 2020/12/10
 * @description :TODO
 */
public class SQLtest {
    public static void main(String[] args){
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        String driveName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/filesystem.document?serverTimezone=UTC";
        String user = "root";
        String password = "Snake010729!";

            try {
                Class.forName(driveName);
                connection = DriverManager.getConnection(url,user,password);
                statement = connection.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                String sql = "select * from user_info";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String pwd = resultSet.getString("password");
                    String role = resultSet.getString("role");
                    System.out.println(username + ";" + pwd + ";" + role);
                }
                resultSet.close();
                statement.close();
                connection.close();
                } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("数据库驱动错误");
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.out.println("数据库错误");
            }
    }
}
