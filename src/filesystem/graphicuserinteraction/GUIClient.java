package filesystem.graphicuserinteraction;

import Network.CommandLineClient;
import operation.AbstractMessage;
import operation.AbstractRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class GUIClient {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    public static final String SERVER_HOST;
    public static final int PORT;

    static {
        Properties properties = new Properties();
        InputStream in = CommandLineClient.class.getClassLoader()
                .getResourceAsStream("CSnet/clientInfo.properties");
        try {
            properties.load(in);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SERVER_HOST = properties.getProperty("serverHost");
        PORT = Integer.parseInt(properties.getProperty("port"));
    }

    public AbstractMessage connectToServer(AbstractRequest request) {
        try {
            client = new Socket(SERVER_HOST, PORT);
            getStreams();
            output.writeObject(request);
            var message = (AbstractMessage)input.readObject();
            return message;
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

    public void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
    }

    private void closeConnection(){
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }
}
