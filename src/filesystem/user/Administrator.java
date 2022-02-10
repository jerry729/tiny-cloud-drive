package filesystem.user;

import util.JDBCUtils;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * @author frank
 */
public class Administrator extends AbstractUser
{

    public Administrator(String name, String password, String role) {
        super(name, password, role);
    }
}



