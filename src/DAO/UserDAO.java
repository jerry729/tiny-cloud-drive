package DAO;


import filesystem.user.AbstractUser;


import java.util.List;

/**
 * @author frank
 */
public interface UserDAO {

    /**
     * @author      :riri
     * @date        :2020/12/13 10:31
     * @description :TODO 按用户名密码查找 未找到时返回 null
     *
     * @throws      :
     * @parameter   :connection
    user
     * @return      :User.AbstractUser
     */
    AbstractUser getUser(String name, String password);


    /**
     * @author      :riri
     * @date        :2020/12/13 10:30
     * @description :TODO 按用户名查找 未找到时返回 null
     *
     * @throws      :
     * @parameter   :connection
    user
     * @return      :User.AbstractUser
     */
    AbstractUser checkUsername(String name);


    /**
     * @author      :riri
     * @date        :2020/12/13 10:53
     * @description :TODO 插入用户
     *
     * @throws      :
     * @parameter   :connection
    user
     * @return      :void
     */
    boolean insertUser(AbstractUser user);


    /**
     * @author      :riri
     * @date        :2020/12/13 10:56
     * @description :TODO 修改用户信息
     *
     * @throws      :
     * @parameter   :connection
    user
     * @return      :boolean
     */
    boolean updateUser( AbstractUser user,String nName, String nPassword, String nRole);


    /**
     * @author      :riri
     * @date        :2020/12/13 11:01
     * @description :TODO
     *
     * @throws      :
     * @parameter   :connection
    user
     * @return      :boolean
     */
    boolean deleteUser( AbstractUser user);


    /**
     * @author      :riri
     * @date        :2020/12/13 11:05
     * @description :TODO
     *
     * @throws      :
     * @parameter   :connection
     * @return      :java.util.ArrayList<User.AbstractUser>
     */
    List<AbstractUser> listAllUser();

}
