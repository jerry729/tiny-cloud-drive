package filesystem.graphicuserinteraction;

import DAO.implement.UserDAOImpl;
import filesystem.document.Doc;
import operation.*;
import filesystem.user.AbstractUser;
import util.JDBCUtils;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author :frank
 * @date :20:31 2020/12/4
 * @description :TODO
 */
public class UserGUI extends JFrame{

    private JPanel contentPanel;
    private JButton uploadButton;
    private JButton downloadButton;
    private JButton returnButton;
    private JButton deleteButton;
    private JPanel userManagementPanel;
    private JPanel filesManagementPanel;
    private JPanel selfInfoManagementPanel;
    private JTabbedPane tabbedPane;
    private static JTable table;
    private DefaultTableModel UserManageTableModel;
    private static DefaultTableModel FileManageTableModel;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JLabel usernameLabel;
    private JLabel oldPasswordLabel;
    private JLabel updatePasswordLabel;
    private JLabel confirmPasswordLabel;
    private JLabel roleLabel;
    private JTextField usernameText;
    private JPasswordField oldPasswordText;
    private JPasswordField updatePasswordText;
    private JPasswordField confirmPasswordText;
    private JTextField roleText;
    private JButton selfInfoUpdateButton;
    private JPanel selfInfoButtonPanel;
    private JLabel userAddUsernameLabel;
    private JLabel userAddPasswordLabel;
    private JLabel userAddRoleLabel;
    private JLabel userUpdateUsernameLabel;
    private JLabel userUpdatePasswordLabel;
    private JLabel userUpdateRoleLabel;
    private JTextField userAddUsernameText;
    private JPasswordField userAddPasswordText;
    private JPasswordField userUpdatePasswordText;
    private JComboBox<String> userAddRoleBox;
    private JComboBox<String> userUpdateRoleBox;
    private JComboBox<String> usernameBox;
    private JButton userAddButton;
    private JButton userUpdateButton;
    private JPanel userAddPanel;
    private JPanel userDeletePanel;
    private JPanel userUpdatePanel;
    private JPanel userAddButtonPanel;
    private JPanel userUpdateButtonPanel;
    private JTable userDeleteTable;
    private JScrollPane userDeleteScrollPanel;
    private JButton userDeleteButton;
    private JPanel userDeleteButtonPanel;
    private UploadFilesGUI uploadFilesGUI;
    private static GUIClient guiClient = new GUIClient();

    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            UserGUI UserGUI = new UserGUI();
            UserGUI.setTitle("管理员界面");
            UserGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            UserGUI.setVisible(true);
        });
    }

    public UserGUI(){

        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        setContentPane(contentPanel);

        tabbedPane = new JTabbedPane();
        userManagementPanel = new JPanel();
        filesManagementPanel = new JPanel();
        selfInfoManagementPanel = new JPanel();
        userManagementPanel.setLayout(new GridBagLayout());
        filesManagementPanel.setLayout(new GridBagLayout());
        selfInfoManagementPanel.setLayout(new GridBagLayout());

        tabbedPane.addTab("用户管理器",userManagementPanel);
        tabbedPane.addTab("文件管理器",filesManagementPanel);
        tabbedPane.addTab("个人信息管理",selfInfoManagementPanel);

        String[] names = {"档案号","创建者","时间","概述","文件名"};
        FileManageTableModel = new DefaultTableModel();
        FileManageTableModel.setColumnIdentifiers(names);
        table = new JTable(FileManageTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        updateFileTable();

        scrollPane = new JScrollPane(table);

        filesManagementPanel.add(scrollPane,new GBC(0,0).setFill(GridBagConstraints.BOTH)
                .setInsets(0,0,10,0));

        uploadButton = new JButton("上传文件");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadFilesGUI = new UploadFilesGUI();
                uploadFilesGUI.setVisible(true);
            }
        });
        downloadButton = new JButton("下载文件");
        returnButton = new JButton("退出");
        deleteButton = new JButton("删除文件");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexOfRow = table.getSelectedRow();
                int id = (int) table.getValueAt(indexOfRow,0);
                String creator = String.valueOf(table.getValueAt(indexOfRow,1));
                Timestamp timestamp = (Timestamp) table.getValueAt(indexOfRow,2);
                String description = String.valueOf(table.getValueAt(indexOfRow,3));
                String fileName = String.valueOf(table.getValueAt(indexOfRow,4));
                Doc doc = new Doc(id,creator,timestamp,description,fileName);

                var message = (DeleteDocMessage)getGuiClient()
                        .connectToServer(new DeleteDocRequest(doc));
                if (message.isRequestSuccessful()){
                    updateFileTable();
                    JOptionPane.showMessageDialog(null,"删除文件成功","完成",JOptionPane.PLAIN_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(null,"服务端删除文件时出错","错误",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int indexOfRow = table.getSelectedRow();
                if (indexOfRow == -1){
                    JOptionPane.showMessageDialog(contentPanel,"没有选择文件噢！",
                            "提示",JOptionPane.WARNING_MESSAGE);
                }
                else {
                    Path fromPath;
                    Path toPath = null;
                    JFileChooser saveChooser = new JFileChooser();
                    saveChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int value = saveChooser.showSaveDialog(contentPanel);
                    String filename = String.valueOf(table.getValueAt(indexOfRow,4));
                    int fileID = (int) table.getValueAt(indexOfRow,0);
                    fromPath = Paths.get(filename);
                    String  path = String.valueOf(fromPath.getFileName());
                    if(value == JFileChooser.APPROVE_OPTION){
                        File file = saveChooser.getSelectedFile();
                        String path1 = String.valueOf(Paths.get(file.getAbsolutePath()));
                        toPath = Paths.get(path1,path);
                        var message = (DownloadMessage)UserGUI.getGuiClient()
                                .connectToServer(new DownloadRequest(fileID));
                        if (message.isRequestSuccessful()){
                            try {
                                BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(toPath));
                                final int bufSize = 1<<10;
                                byte[] buf = new byte[bufSize];
                                Doc doc = message.getDoc();
                                var input = UserGUI.getGuiClient().getInput();
                                int end;
                                while ((end = input.read(buf)) != -1){
                                    input.readNBytes(buf,0,bufSize);
                                    out.write(buf);
                                }
                                JOptionPane.showMessageDialog(null,"下载完成","完成",
                                        JOptionPane.PLAIN_MESSAGE);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                                JOptionPane.showMessageDialog(null,"读取文件时出错","错误",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(uploadButton,new GBC(0,0));
        buttonPanel.add(downloadButton,new GBC(1,0));
        buttonPanel.add(deleteButton,new GBC(2,0));
        buttonPanel.add(returnButton,new GBC(3,0));

        ActionListener listener =event -> Quit();
        returnButton.addActionListener(listener);

        filesManagementPanel.add(buttonPanel,new GBC(0,1).setFill(GBC.HORIZONTAL));

        usernameLabel = new JLabel("用户名：");
        oldPasswordLabel = new JLabel("原密码：");
        updatePasswordLabel = new JLabel("新密码：");
        confirmPasswordLabel = new JLabel("确认密码：");
        roleLabel = new JLabel("权限：");

        usernameText = new JTextField();
        oldPasswordText = new JPasswordField();
        updatePasswordText = new JPasswordField();
        confirmPasswordText = new JPasswordField();
        roleText = new JTextField();
        usernameText.setColumns(15);
        oldPasswordText.setColumns(15);
        updatePasswordText.setColumns(15);
        confirmPasswordText.setColumns(15);
        roleText.setColumns(15);

        selfInfoUpdateButton = new JButton("修改");

        selfInfoButtonPanel = new JPanel();
        selfInfoButtonPanel.setLayout(new GridBagLayout());
        selfInfoButtonPanel.add(selfInfoUpdateButton,new GBC(0,0)
                .setInsets(10,0,10,5));

        selfInfoManagementPanel.add(usernameLabel,new GBC(0,0).setAnchor(GBC.EAST)
                .setInsets(0,0,0,10));
        selfInfoManagementPanel.add(oldPasswordLabel,new GBC(0,1).setAnchor(GBC.EAST)
                .setInsets(0,0,0,10));
        selfInfoManagementPanel.add(updatePasswordLabel,new GBC(0,2).setAnchor(GBC.EAST)
                .setInsets(0,0,0,10));
        selfInfoManagementPanel.add(confirmPasswordLabel,new GBC(0,3).setAnchor(GBC.EAST)
                .setInsets(0,0,0,10));
        selfInfoManagementPanel.add(roleLabel,new GBC(0,4).setAnchor(GBC.EAST)
                .setInsets(0,0,0,10));
        selfInfoManagementPanel.add(usernameText,new GBC(1,0).setAnchor(GBC.WEST));
        selfInfoManagementPanel.add(oldPasswordText,new GBC(1,1).setAnchor(GBC.WEST));
        selfInfoManagementPanel.add(updatePasswordText,new GBC(1,2).setAnchor(GBC.WEST));
        selfInfoManagementPanel.add(confirmPasswordText,new GBC(1,3).setAnchor(GBC.WEST));
        selfInfoManagementPanel.add(roleText,new GBC(1,4).setAnchor(GBC.WEST));
        selfInfoManagementPanel.add(selfInfoButtonPanel,new GBC(0,5,2,1));

        userAddUsernameLabel = new JLabel("用户名：");
        userAddPasswordLabel = new JLabel("密码：");
        userAddRoleLabel = new JLabel("权限：");
        userAddUsernameText = new JTextField();
        userAddUsernameText.setColumns(15);
        userAddPasswordText = new JPasswordField();
        userAddPasswordText.setColumns(15);
        String[] RoleItem = {"Administrator","Operator","Browser"};
        userAddRoleBox = new JComboBox<>(RoleItem);
        String[] usernameItem = {"jack","rose","root"};
        usernameBox = new JComboBox<>(usernameItem);
        updateUsernameBox();

        userAddPanel = new JPanel();
        userAddPanel.setLayout(new GridBagLayout());
        userAddPanel.add(userAddUsernameLabel,new GBC(0,0));
        userAddPanel.add(userAddPasswordLabel,new GBC(0,1));
        userAddPanel.add(userAddRoleLabel,new GBC(0,2));
        userAddPanel.add(userAddUsernameText,new GBC(1,0).setAnchor(GBC.WEST));
        userAddPanel.add(userAddPasswordText,new GBC(1,1).setAnchor(GBC.WEST));
        userAddPanel.add(userAddRoleBox,new GBC(1,2).setAnchor(GBC.WEST));

        userAddButton = new JButton("添加");
        userAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uname = userAddUsernameText.getText();
                String  uPasswd = new String(userAddPasswordText.getPassword()) ;
                String uRole = String.valueOf(userAddRoleBox.getSelectedItem());
                AbstractUser user = new AbstractUser(uname,uPasswd,uRole);
                var message = (AddUserMessage)getGuiClient()
                        .connectToServer(new AddUserRequest(user));
                if(message.isRequestSuccessful()){
                    updateUserTable();
                    userAddUsernameText.setText("");
                    userAddPasswordText.setText("");
                    userAddRoleBox.setSelectedIndex(0);
                    updateUsernameBox();
                }else {
                    JOptionPane.showMessageDialog(getUserGui(),"服务器端出问题","错误",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        userAddButtonPanel = new JPanel();
        userAddButtonPanel.setLayout(new GridBagLayout());
        userAddButtonPanel.add(userAddButton,new GBC(0,0).setFill(GBC.BOTH));
        userAddPanel.add(userAddButtonPanel,new GBC(0,3,2,1));

        userAddPanel.setBorder(BorderFactory.createTitledBorder("添加用户"));

        userUpdateUsernameLabel = new JLabel("用户名：");
        userUpdatePasswordLabel = new JLabel("密码：");
        userUpdateRoleLabel = new JLabel("权限：");

        userUpdatePasswordText = new JPasswordField();
        userUpdatePasswordText.setColumns(15);
        userUpdateRoleBox = new JComboBox<>(RoleItem);

        userUpdateButton = new JButton("修改");
        userUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uname1 = String.valueOf(usernameBox.getSelectedItem());
                String uPassword = new String(userUpdatePasswordText.getPassword());
                String uRole = String.valueOf(userUpdateRoleBox.getSelectedItem());
                AbstractUser user = new AbstractUser(uname1,uPassword,uRole);
                var message = (UpdateUserMessage)getGuiClient()
                        .connectToServer(new UpdateUserRequest(user,uPassword,uRole));
                if (message.isRequestSuccessful()){
                    updateUserTable();
                    usernameBox.setSelectedIndex(0);
                    userUpdateRoleBox.setSelectedIndex(0);
                    userUpdatePasswordText.setText("");
                    updateUsernameBox();
                }else {
                    JOptionPane.showMessageDialog(getUserGui(),"服务器发生了错误","错误",
                                JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        userUpdateButtonPanel = new JPanel();
        userUpdateButtonPanel.setLayout(new GridBagLayout());
        userUpdateButtonPanel.add(userUpdateButton,new GBC(0,0).setFill(GBC.BOTH));

        userUpdatePanel = new JPanel();
        userUpdatePanel.setLayout(new GridBagLayout());
        userUpdatePanel.add(userUpdateUsernameLabel,new GBC(0,0));
        userUpdatePanel.add(userUpdatePasswordLabel,new GBC(0,1));
        userUpdatePanel.add(userUpdateRoleLabel,new GBC(0,2));
        userUpdatePanel.add(usernameBox,new GBC(1,0).setAnchor(GBC.WEST));
        userUpdatePanel.add(userUpdatePasswordText,new GBC(1,1).setAnchor(GBC.WEST));
        userUpdatePanel.add(userUpdateRoleBox,new GBC(1,2).setAnchor(GBC.WEST));
        userUpdatePanel.add(userUpdateButtonPanel,new GBC(1,3,2,1));
        userUpdatePanel.setBorder(BorderFactory.createTitledBorder("修改用户"));

        String[] names1 = {"用户名","口令","角色"};
        UserManageTableModel = new DefaultTableModel();
        UserManageTableModel.setColumnIdentifiers(names1);
        userDeleteTable = new JTable(UserManageTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        updateUserTable();

        userDeleteScrollPanel = new JScrollPane(userDeleteTable);
        userDeleteButton = new JButton("删除");
        userDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexOfRow = userDeleteTable.getSelectedRow();
                String userName = String.valueOf(userDeleteTable.getValueAt(indexOfRow,
                        0));
                String passwd = String.valueOf(userDeleteTable.getValueAt(indexOfRow,
                        1));
                String role = String.valueOf(userDeleteTable.getValueAt(indexOfRow,
                        2));
                AbstractUser user = new AbstractUser(userName,passwd,role);
                var message = (DeleteUserMessage)getGuiClient()
                        .connectToServer(new DeleteUserRequest(user));
                if (message.isRequestSuccessful()){
                    updateUserTable();
                    updateUsernameBox();
                }else {
                    JOptionPane.showMessageDialog(getUserGui(),"服务器端错误","错误",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        userDeleteButtonPanel = new JPanel();
        userDeleteButtonPanel.setLayout(new GridBagLayout());
        userDeleteButtonPanel.add(userDeleteButton,new GBC(0,0)
                .setFill(GBC.BOTH));

        userDeletePanel = new JPanel();
        userDeletePanel.setLayout(new GridBagLayout());
        userDeletePanel.add(userDeleteScrollPanel,new GBC(0,0)
                .setFill(GBC.BOTH));
        userDeletePanel.add(userDeleteButtonPanel,new GBC(0,1));
        userDeletePanel.setBorder(BorderFactory.createTitledBorder("删除用户"));

        userManagementPanel.add(userAddPanel,new GBC(0,0)
                .setFill(GBC.BOTH));
        userManagementPanel.add(userUpdatePanel,new GBC(1,0)
                .setFill(GBC.BOTH));
        userManagementPanel.add(userDeletePanel,new GBC(2,0)
                .setFill(GBC.BOTH));

        contentPanel.add(tabbedPane,new GBC(0,0)
                .setFill(GridBagConstraints.BOTH));
        pack();

    }

    private static final int DEFAULT_WIDTH = 850;
    private static final int DEFAULT_HEIGHT = 400;
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    public void setUploadButtonDisabled(){
        this.uploadButton.setEnabled(false);
    }

    public void deleteTab(int index){
        this.tabbedPane.remove(index);
    }

    public void setInitialInfo(){
       roleText.setText(LoginGUI.getLoginRole());
       usernameText.setText(LoginGUI.getLoginUsername());
       roleText.setEnabled(false);
       usernameText.setEnabled(false);
    }

    public String getOldPassword(){
        String oldPassword = new String(oldPasswordText.getPassword());
        return oldPassword;
    }

    public String getUpdatePassword(){
        String updatePassword = new String(updatePasswordText.getPassword());
        return updatePassword;
    }

    public String getConfirmPassword(){
        String confirmPassword = new String(confirmPasswordText.getPassword());
        return confirmPassword;
    }

    public UploadFilesGUI getUploadFilesGUI() {
        return uploadFilesGUI;
    }

    public boolean changeSelfInfo() throws SQLException {

        String oldPassword = getOldPassword();
        String updatePassword = getUpdatePassword();
        String confirmPassword = getConfirmPassword();
        String username = usernameText.getText();

        JDBCUtils.init();
        if (new UserDAOImpl().checkUsername(usernameText.getText())
                .getPassword().equalsIgnoreCase(oldPassword)) {
            if (!updatePassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null,
                        "两次输入密码不一致哦",
                        "输入错误", JOptionPane.ERROR_MESSAGE);
            }
            AbstractUser user = new UserDAOImpl().getUser(username,oldPassword);
            if (user != null) {
                String role = user.getRole();
                AbstractUser user1 = new AbstractUser(username,oldPassword,role);
                var message = (SelfModMessage)getGuiClient()
                        .connectToServer(new SelfModRequest(user1,updatePassword));
                if (message.isRequestSuccessful()){
                    updateUserTable();
                    JOptionPane.showMessageDialog(null, "修改成功啦",
                            "完成", JOptionPane.PLAIN_MESSAGE);
                    oldPasswordText.setText("");
                    updatePasswordText.setText("");
                    confirmPasswordText.setText("");
                    return true;
                }else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "原密码输入错误",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void changeButton() {
        selfInfoUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    changeSelfInfo();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
    }

    public void Quit(){
        this.setVisible(false);
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

    public static void updateFileTable(){
        String[] names = {"档案号","创建者","时间","概述","文件名"};
        FileManageTableModel = new DefaultTableModel();
        FileManageTableModel.setColumnIdentifiers(names);
        ArrayList<Doc> list = new ArrayList<Doc>();
        var message = (ListDocMessage)getGuiClient()
                .connectToServer(new ListDocRequest());
        list = (ArrayList<Doc>) message.getDocList();
        for (int i = 0; i < list.size(); i++){
            Vector<java.io.Serializable> row = new Vector<>();
            row.add(list.get(i).getId());
            row.add(list.get(i).getCreator());
            row.add(list.get(i).getTimestamp());
            row.add(list.get(i).getDescription());
            row.add(list.get(i).getFilename());
            FileManageTableModel.addRow(row);
        }
        table.setModel(FileManageTableModel);
    }

    public void updateUserTable(){
        String[] names1 = {"用户名","口令","角色"};
        UserManageTableModel = new DefaultTableModel();
        UserManageTableModel.setColumnIdentifiers(names1);
        ArrayList<AbstractUser> userArrayList = new ArrayList<AbstractUser>();
        var message = (ListUserMessage)getGuiClient()
                .connectToServer(new ListUserRequest());
        userArrayList = (ArrayList<AbstractUser>) message.getUserList();
        for (int i = 0; i < userArrayList.size(); i++){
            Vector row = new Vector();
            row.add(userArrayList.get(i).getName());
            row.add(userArrayList.get(i).getPassword());
            row.add(userArrayList.get(i).getRole());
            UserManageTableModel.addRow(row);
        }
        userDeleteTable.setModel(UserManageTableModel);
    }

    public void updateUsernameBox(){
        usernameBox.removeAllItems();
        ArrayList<AbstractUser> list = new ArrayList<AbstractUser>();
        var message = (ListUserMessage)getGuiClient()
                .connectToServer(new ListUserRequest());
        list = (ArrayList<AbstractUser>) message.getUserList();
        for (int i = 0; i < list.size(); i++){
            String name = list.get(i).getName();
            usernameBox.insertItemAt(name,i);
        }
    }

    public static GUIClient getGuiClient() {
        return guiClient;
    }

    public UserGUI getUserGui(){
        return this;
    }
}


