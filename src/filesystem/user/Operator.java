package filesystem.user;


import DAO.implement.DocDAOImpl;
import filesystem.document.Doc;
import filesystem.graphicuserinteraction.LoginGUI;
import filesystem.graphicuserinteraction.UploadFilesGUI;

import java.io.IOException;



/**
 * @author frank
 */
public class Operator extends AbstractUser
{

    public Operator(String name, String password, String role) {
        super(name, password, role);
    }

}
