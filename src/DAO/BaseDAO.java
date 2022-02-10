package DAO;


import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :frank
 * @date :21:05 2020/12/12
 * @description :TODO
 */
public abstract class BaseDAO<T> {

    private Class<T> type;

    public BaseDAO(){
        Class clazz = this.getClass();

        ParameterizedType parameterizedType = (ParameterizedType)clazz
                .getGenericSuperclass();
       Type[] types = parameterizedType.getActualTypeArguments();
       this.type = (Class<T>)types[0];

    }

    public void update(String sql, Object ... args){

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCUtils.init();
            preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            for (int i = 0; i < args.length; i++){
                preparedStatement.setObject(i+1,args[i]);
            }
            preparedStatement.execute();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            JDBCUtils.disconnectFromDataBase(preparedStatement,connection);
        }
    }

    public  <T> T getInstance(Class<T> clazz, String sql, Object ... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCUtils.init();
            preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            for (int i = 0; i < args.length; i++){
                preparedStatement.setObject(i + 1,args[i]);
            }

            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int columnCount = resultSetMetaData.getColumnCount();
            if (resultSet.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++){
                    Object columnValue = resultSet.getObject(i + 1);
                    String columnLabel = resultSetMetaData.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                    
                }
                return t;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.disconnectFromDataBase(resultSet,preparedStatement,connection);
        }
        return null;
    }

    public  <T> List<T> getInstanceList(Class<T> clazz, String sql, Object ... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCUtils.init();
            preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            for (int i = 0; i < args.length; i++){
                preparedStatement.setObject(i + 1,args[i]);
            }

            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int columnCount = resultSetMetaData.getColumnCount();
            List<T> list = new ArrayList<>();
            while (resultSet.next()){
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++){
                    Object columnValue = resultSet.getObject(i + 1);
                    String columnLabel = resultSetMetaData.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.disconnectFromDataBase(resultSet,preparedStatement,connection);
        }
        return null;
    }

}
