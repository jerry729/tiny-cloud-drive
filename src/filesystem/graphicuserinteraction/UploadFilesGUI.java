package filesystem.graphicuserinteraction;

import filesystem.document.Doc;
import operation.UploadRequest;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;

public class UploadFilesGUI extends JFrame {
    private JPanel contentPanel;
    private JLabel docNOLabel;
    private JLabel docDescriptionLabel;
    private JLabel docNameLabel;
    private JTextField docNOText;
    private JTextField docNameText;
    private JTextArea docDescriptionText;
    private JButton uploadButton;
    private JButton cancelButton;
    private JButton openButton;
    private JPanel buttonPanel;
    private JFileChooser openChooser;
    private int value;
    private Path clientPath;

    public UploadFilesGUI() {
        contentPanel = new JPanel();
        setContentPane(contentPanel);
        contentPanel.setLayout(new GridBagLayout());

        docNOLabel = new JLabel("档案号");
        docDescriptionLabel = new JLabel("档案描述");
        docNameLabel = new JLabel("档案文件名");

        docNOText = new JTextField(15);
        docNameText = new JTextField(15);
        docDescriptionText = new JTextArea(5, 15);

        uploadButton = new JButton("上传");
        cancelButton = new JButton("取消");
        openButton = new JButton("打开");

        ActionListener listener = event -> cancel();
        cancelButton.addActionListener(listener);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(uploadButton);
        buttonPanel.add(cancelButton);

        contentPanel.add(docNOLabel, new GBC(0, 0));
        contentPanel.add(docDescriptionLabel, new GBC(0, 1));
        contentPanel.add(docNameLabel, new GBC(0, 2));
        contentPanel.add(docNOText, new GBC(1, 0));
        contentPanel.add(docDescriptionText, new GBC(1, 1));
        contentPanel.add(docNameText, new GBC(1, 2));
        contentPanel.add(openButton, new GBC(2, 2));
        contentPanel.add(buttonPanel, new GBC(0, 3, 3, 1)
                .setFill(GBC.BOTH));

        pack();

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openChooser = new JFileChooser();
                openChooser.setMultiSelectionEnabled(true);
                value = openChooser.showOpenDialog(contentPanel);
                if (value == JFileChooser.APPROVE_OPTION) {
                    File[] files = openChooser.getSelectedFiles();
                    for (int i = 0; i < files.length; i++) {
                        docNameText.setText(docNameText.getText() +
                                files[i].getName());
                        clientPath = Path.of(files[i].getAbsolutePath());
                    }
                } else if (value == JFileChooser.CANCEL_OPTION) {
                    docNameText.setText("未选择文件");
                }
            }
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                long fileSize;
                try {
                    fileSize = Files.size(clientPath);
                    String docName = docNameText.getText();
                    String docDescription = docDescriptionText.getText();
                    int docNo = Integer.parseInt(docNOText.getText());
                    String creator = LoginGUI.getLoginUsername();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Doc doc = new Doc(docNo,creator,timestamp,docDescription,docName);

                    var message = UserGUI.getGuiClient().connectToServer(new UploadRequest(doc,fileSize));
                    if (message.isRequestSuccessful()){
                        BufferedInputStream input = new BufferedInputStream(Files.newInputStream(clientPath));
                        int bufSize = 1<<10;
                        byte[] buf = new byte[bufSize];
                        int end;
                        while ((end = input.read(buf)) != -1){
                            UserGUI.getGuiClient().getOutput().write(buf,0,bufSize);
                        }
                        JOptionPane.showMessageDialog(null,"上传文件成功","完成",
                                JOptionPane.PLAIN_MESSAGE);
                    }else {
                        JOptionPane.showMessageDialog(null,"上传时服务端错误","错误",JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(null,"获取文件大小错误","错误",
                            JOptionPane.WARNING_MESSAGE);
                }
                UserGUI.updateFileTable();
                docNameText.setText("");
                docDescriptionText.setText("");
                docNOText.setText("");
            }
        });

    }

    public void cancel(){
        this.setVisible(false);
    }

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 220;

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public JTextField getDocNOText() {
        return docNOText;
    }

    public JTextField getDocNameText() {
        return docNameText;
    }

    public JTextArea getDocDescriptionText() {
        return docDescriptionText;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            UploadFilesGUI uploadFilesGUI = new UploadFilesGUI();
            uploadFilesGUI.setTitle("上传文件");
            uploadFilesGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            uploadFilesGUI.setVisible(true);
        });
    }

}
