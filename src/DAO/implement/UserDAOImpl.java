package DAO.implement;

import DAO.BaseDAO;
import DAO.UserDAO;
import filesystem.user.AbstractUser;

import java.util.List;

/**
 * @author :frank
 * @date :11:30 2020/12/13
 * @description :TODO
 */
public class UserDAOImpl extends BaseDAO<AbstractUser> implements UserDAO {

    @Override
    public AbstractUser getUser(String name,String password) {

        AbstractUser abstractUser = null;
        String sql =
                "SELECT username,password,role " +
                "FROM user_info " +
                "WHERE username = ? AND password = ?";
        abstractUser = getInstance(AbstractUser.class,sql,
                name, password);
        return abstractUser;
    }

    @Override
    public AbstractUser checkUsername(String name) {
        AbstractUser abstractUser = null;
        String sql =
                "SELECT username,password,role " +
                "FROM user_info " +
                "WHERE username = ?";
        abstractUser = getInstance(AbstractUser.class,sql,name);
        return abstractUser;
    }

    @Override
    public boolean insertUser(AbstractUser user) {
        String sql =
                "INSERT INTO user_info(username,password,role)" +
                "VALUES(?,?,?)";
        update(sql,user.getName(),user.getPassword(),user.getRole());
        return true;
    }

    @Override
    public boolean updateUser(AbstractUser user, String nName,
                              String nPassword, String nRole) {

        String sql =
                "UPDATE user_info " +
                "SET username = ?,password = ?,role = ? " +
                "WHERE username = ?";
        update(sql,nName,nPassword,nRole,user.getName());
        return true;
    }

    @Override
    public boolean deleteUser(AbstractUser user) {
        String sql =
                "DELETE FROM user_info " +
                "WHERE username = ?";
        update(sql,user.getName());
        return true;
    }

    @Override
    public List<AbstractUser> listAllUser() {
        String sql = "SELECT * FROM user_info";
        List<AbstractUser> userList = getInstanceList(AbstractUser.class,sql);
        return  userList;
    }
}
