package filesystem.user;


import java.io.Serializable;

/**
 * @author frank
 */
public class AbstractUser implements Serializable {
    private String username;
    private String password;
    private String role;

    public AbstractUser(){}

    public AbstractUser(String name, String password, String role) {

        this.username = name;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return username + " " + password + " " + role;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof AbstractUser){
            AbstractUser user = (AbstractUser) object;
            if (user.getName() == null || username == null){
                return false;
            }
            if (user.getPassword() == null || password == null){
                return false;
            }
            if (user.getRole() == null || role == null){
                return false;
            }
            else {
                return username.equalsIgnoreCase(user.getName())
                        && password.equals(user.getPassword())
                        && role.equals(user.getRole());
            }
        }
        return false;
    }

}