package filesystem.graphicuserinteraction;

import DAO.implement.UserDAOImpl;
import operation.AbstractMessage;
import operation.LoginMessage;
import operation.LoginRequest;
import filesystem.user.AbstractUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * @author :frank
 * @date :16:46 2020/12/9
 * @description :TODO
 */
public class LoginGUI extends JFrame {

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private static JTextField usernameText;
    private JPasswordField passwordText;
    private JButton loginButton;
    private JButton exitButton;
    private JPanel TextPanel;
    private JPanel LabelPanel;
    private JPanel buttonPanel;
    private ActionListener listener1;
    private ActionListener listener2;


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            LoginGUI logingui = new LoginGUI();
            logingui.setTitle("用户登录");
            logingui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            logingui.setResizable(false);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            Point p = new Point((d.width - logingui.getWidth()) / 2,
                    (int) ((d.getHeight() - logingui.getHeight()) / 2));
            logingui.setLocation(p);
            logingui.setVisible(true);
        });
    }

    public LoginGUI() {

        getPreferredSize();
        pack();

        JPanel contentPanel = new JPanel();
        setContentPane(contentPanel);

        contentPanel.setLayout(new GridBagLayout());

        usernameLabel = new JLabel("用户名:");
        passwordLabel = new JLabel("密码:");

        usernameText = new JTextField();
        passwordText = new JPasswordField();
        usernameText.setColumns(15);
        passwordText.setColumns(15);

        TextPanel = new JPanel();
        LabelPanel = new JPanel();
        TextPanel.setLayout(new GridBagLayout());
        LabelPanel.setLayout(new GridBagLayout());

        LabelPanel.add(usernameLabel, new GBC(0, 0).setInsets(0, 0, 10, 0)
                .setAnchor(GBC.EAST));
        LabelPanel.add(passwordLabel, new GBC(0, 1).setInsets(0, 0, 0, 0)
                .setAnchor(GBC.EAST));
        TextPanel.add(usernameText, new GBC(0, 0)
                .setInsets(0, 0, 10, 0));
        TextPanel.add(passwordText, new GBC(0, 1)
                .setInsets(0, 0, 0, 0));

        loginButton = new JButton("登录");
        exitButton = new JButton("退出");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(loginButton, new GBC(0, 0)
                .setInsets(0, 20, 20, 20));
        buttonPanel.add(exitButton, new GBC(1, 0)
                .setInsets(0, 20, 20, 20));

        contentPanel.add(LabelPanel, new GBC(0, 0).setFill(GBC.HORIZONTAL));
        contentPanel.add(TextPanel, new GBC(1, 0));
        contentPanel.add(buttonPanel, new GBC(0, 2, 2, 1));

        ActionListener listener1 = event -> Log();
        loginButton.addActionListener(listener1);

        listener2 = event -> System.exit(0);
        exitButton.addActionListener(listener2);

    }

    public void Log() {
       final UserGUI userGUI = new UserGUI();
        String usernameInput = usernameText.getText();
        String passwordInput = new String(passwordText.getPassword());

            String role = new UserDAOImpl().checkUsername(usernameInput).getRole();
            AbstractUser user = new AbstractUser(usernameInput,passwordInput,role);
            switch (role.toLowerCase()){
                case "administrator":
                    EventQueue.invokeLater(()->{

                        userGUI.setTitle("管理员界面");
                        userGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        userGUI.setInitialInfo();
                        userGUI.changeButton();
                        logTrans(userGUI,user);
                    });
                    break;
                case "operator":
                    EventQueue.invokeLater(()->{

                        userGUI.setTitle("操作员界面");
                        userGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        userGUI.setInitialInfo();
                        userGUI.deleteTab(0);
                        userGUI.changeButton();
                        logTrans(userGUI,user);
                    });
                    break;
                case "browser":
                    EventQueue.invokeLater(()->{

                        userGUI.setTitle("浏览员界面");
                        userGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        userGUI.setInitialInfo();
                        userGUI.deleteTab(0);
                        userGUI.setUploadButtonDisabled();
                        userGUI.changeButton();
                        logTrans(userGUI,user);
                    });
                default:
            }
    }

    public AbstractMessage logTrans(UserGUI userGUI,AbstractUser user){
        var message = (LoginMessage)(UserGUI.getGuiClient().connectToServer(new LoginRequest(user)));
        if (!message.isRequestSuccessful()){
            JOptionPane.showMessageDialog(null,"用户名或密码错误");
        }else {
            userGUI.setVisible(true);
            this.setVisible(false);
        }
        return message;
    }

    public static String getLoginUsername() {
        return usernameText.getText();
    }

    public static String getLoginRole() {
        return new UserDAOImpl().checkUsername(usernameText.getText()).getRole();
    }


    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 220;

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}