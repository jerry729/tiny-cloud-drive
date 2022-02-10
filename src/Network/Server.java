package Network;
import DAO.implement.DocDAOImpl;
import DAO.implement.UserDAOImpl;
import filesystem.document.Doc;
import operation.*;
import filesystem.user.AbstractUser;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

    private ServerSocket serverSocket;
    private Socket connection;
    private int counter = 1;

    public static final String SERVER_DATA_PATH;
    public static final int PORT;
    public static final int CORE_POOL_SIZE = 2 * Runtime.getRuntime().availableProcessors();
    public static final int WORK_QUEUE_SIZE = 2 * CORE_POOL_SIZE;
    public static final int MAXIMUM_POOL_SIZE = 2 * CORE_POOL_SIZE;

    static {
        Properties properties = new Properties();
        InputStream in = Server.class.getClassLoader()
                .getResourceAsStream("CSnet/serverInfo.properties");
        try {
            properties.load(in);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SERVER_DATA_PATH = properties.getProperty("dataPath");
        PORT = Integer.parseInt(properties.getProperty("port"));
    }

    private final ThreadPoolExecutor threadPoolExecutor;

    public Server(){
        threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(WORK_QUEUE_SIZE),
                new ThreadPoolExecutor.CallerRunsPolicy());
        try {
            serverSocket = new ServerSocket(PORT);
            while (true){
                waitForConnection();
                threadPoolExecutor.execute(new ServerHandler(connection));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        threadPoolExecutor.shutdown();
    }

//    public void runServer(){
//        try{
//
//            serverSocket = new ServerSocket(PORT);
//
//            while (true){
//                waitForConnection();
//                Runnable runnable = new ServerHandler(connection);
//                Thread thread = new Thread(runnable);
//                thread.start();
//                counter++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void waitForConnection() throws IOException {
        displayMessage("Waiting for connection");
        connection = serverSocket.accept();
        displayMessage("Connection" + counter + "received from: " +
                connection.getInetAddress().getHostName());
    }

    static void displayMessage(String messageToDisplay){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(messageToDisplay);
                    }
                }
        );
    }


    public static void main(String[] args){
        Server server = new Server();
    }
}


class ServerHandler implements Runnable{
    private Socket connection;
    private AbstractRequest request = null;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;

    public ServerHandler(Socket incomingSocket){
        connection = incomingSocket;
    }

    private void getStreams() throws IOException {
        outputStream = new ObjectOutputStream(connection.getOutputStream());
        outputStream.flush();

        inputStream = new ObjectInputStream(connection.getInputStream());

        Server.displayMessage("Got I/O streams");
    }

    static void sendData(String message)  {

        try {
            outputStream.writeObject("SERVER>>> " + message);
            outputStream.flush();
            Server.displayMessage("SERVER>>>" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(){

        Server.displayMessage("Terminating connection");
        try{
            outputStream.close();
            inputStream.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            getStreams();

            Object object = null;
            try {
                object = inputStream.readObject();
                if (! (object instanceof AbstractRequest)){
                    throw new RequestFormatException("请求格式错误");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (RequestFormatException e) {
                e.printStackTrace();
                writeObjects(new WrongRequestFormatMessage(true));
            }
            request = (AbstractRequest)object;

            switch (request.getType()){
                case LOGIN_OPERATION -> loginHandler();
                case SELF_MOD_OPERATION -> selfModHandler();
                case LIST_ALL_USER_OPERATION -> listUserHandler();
                case LIST_ALL_DOC_OPERATION -> listDocHandler();
                case UPLOAD_OPERATION -> uploadHandler();
                case DOWNLOAD_OPERATION -> downloadHandler();
                case USER_ADD_OPERATION -> addUserHandler();
                case USER_DELETE_OPERATION -> deleterUserHandler();
                case USER_UPDATE_OPERATION -> updateUserHandler();
                case DELETE_DOC_OPERATION -> deleteDocHandler();
                default -> {
                    writeObjects(new UnknownRequestMessage());
                    throw new UnknownRequestException("未知的请求");
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (UnknownRequestException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
    }
    public void writeObjects(Object... objects) throws IOException {
        for (Object each : objects){
            outputStream.writeObject(each);
        }
    }

    public void loginHandler() throws IOException {
        var concreteRequest = (LoginRequest) request;
        AbstractUser user = concreteRequest.getUser();
        AbstractUser checkUser = new UserDAOImpl().checkUsername(user.getName());
        boolean x = checkUser.equals(user);
        if (checkUser == null || !x){
            writeObjects(new LoginMessage(false));
        }else {
            writeObjects(new LoginMessage(true));
        }
    }
    public void selfModHandler() throws IOException {
        var concreteRequest = (SelfModRequest)request;
        AbstractUser user = concreteRequest.getUser();
        String nPassword = concreteRequest.getnPassword();
        boolean isRequestSuccess = new UserDAOImpl().updateUser(user,user.getName(),
                nPassword,user.getRole());
        writeObjects(new SelfModMessage(isRequestSuccess));
    }
    public void listUserHandler() throws IOException {
        var concreteRequest = (ListUserRequest) request;
        writeObjects(new ListUserMessage(new UserDAOImpl().listAllUser()));
    }
    public void listDocHandler() throws IOException {
        var concreteRequest = (ListDocRequest) request;
        writeObjects(new ListDocMessage(new DocDAOImpl().listAllDoc()));
    }
    public void addUserHandler() throws IOException {
        var concreteRequest = (AddUserRequest) request;
        AbstractUser user = concreteRequest.getUserToInsert();
        boolean isRequestSuccess = new UserDAOImpl().insertUser(user);
        writeObjects(new AddUserMessage(isRequestSuccess));
    }
    public void deleterUserHandler() throws IOException {
        var concreteRequest = (DeleteUserRequest)request;
        AbstractUser user = concreteRequest.getUserToDelete();
        boolean isRequestSuccess = new UserDAOImpl().deleteUser(user);
        writeObjects(new DeleteUserMessage(isRequestSuccess));
    }
    public void updateUserHandler() throws IOException {
        var concreteRequest = (UpdateUserRequest)request;
        AbstractUser userToUpdate = concreteRequest.getUserToUpdate();
        String nPassword = concreteRequest.getnPassword();
        String nRole = concreteRequest.getnRole();
        boolean isRequestSuccess = new UserDAOImpl().updateUser(userToUpdate,
                userToUpdate.getName(),nPassword,nRole);
        writeObjects(new UpdateUserMessage(isRequestSuccess));
    }
    public void uploadHandler() throws IOException {
        var concreteRequest = (UploadRequest)request;
        Doc doc = concreteRequest.getDocToUpload();
        if(new DocDAOImpl().insertDoc(doc)){
            writeObjects(new UploadMessage(true));
            Path path = Paths.get(doc.getFilename());
            var serverPath = Paths.get(Server.SERVER_DATA_PATH,
                    String.valueOf(path.getFileName()));
            Files.createFile(serverPath);
            try (BufferedOutputStream out = new BufferedOutputStream(
                    Files.newOutputStream(serverPath));){
                long docSize = concreteRequest.getDocSize();
                final int bufSize = 1<<10;
                byte[] buf = new byte[bufSize];
                while(docSize > bufSize){
                    inputStream.readNBytes(buf,0,bufSize);
                    out.write(buf);
                    docSize -= bufSize;
                }
                if (docSize > 0){
                    inputStream.readNBytes(buf,0,
                            Math.toIntExact(docSize));
                    out.write(buf);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else {
            writeObjects(new UploadMessage(false));
        }
    }
    public void downloadHandler() throws IOException {
        var concreteRequest = (DownloadRequest)request;
        int ID = concreteRequest.getID();
        Doc serverDoc = new DocDAOImpl().getDocByID(ID);
        if (serverDoc == null){
            writeObjects(new DownloadMessage(false,null));
        }else {
            Path path = Path.of(serverDoc.getFilename()).getFileName();
            Path serverPath = Paths.get(Server.SERVER_DATA_PATH,
                    String.valueOf(path));
            if (Files.exists(serverPath)){
                writeObjects(new DownloadMessage(true,
                        serverDoc));
                BufferedInputStream input = new BufferedInputStream(
                        Files.newInputStream(serverPath));
                int bufSize = 1<<10;
                byte[] buf = new byte[bufSize];
                int end;
                while ((end = input.read(buf)) != -1){
                    outputStream.write(buf,0,bufSize);
                }
            }else {
                writeObjects(new DownloadMessage(false,null));
                throw new  NoSuchFileException("找不到文件");
            }
        }
    }
    public void deleteDocHandler() throws IOException {
        var concreteRequest = (DeleteDocRequest)request;
        Doc docToDelete = concreteRequest.getDocToDelete();
        boolean isRequestSuccess = new DocDAOImpl().deleteDoc(docToDelete);
        Path path1 = Paths.get(Server.SERVER_DATA_PATH,docToDelete.getFilename());
        Files.delete(path1);
        writeObjects(new DeleteDocMessage(isRequestSuccess));
    }
}
