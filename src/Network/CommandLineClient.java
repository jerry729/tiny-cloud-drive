package Network;

import operation.AbstractMessage;
import operation.AbstractRequest;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;


public class CommandLineClient {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";

   public static final String SERVER_HOST;
   public static final int PORT;

   static {
       Properties properties = new Properties();
       InputStream in = CommandLineClient.class.getClassLoader()
               .getResourceAsStream("clientInfo.properties");
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
            displayMessage("Attempting connection");
            client = new Socket(SERVER_HOST, PORT);
            displayMessage("Connected to: " +
                    client.getInetAddress().getHostName());
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
        displayMessage("Got I/O streams");
    }


    private void closeConnection(){
        displayMessage("\nClosing connection");
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData(String message){
        try{
            output.writeUTF("CLIENT>>> " + message);
            output.flush();
            displayMessage("\nCLIENT>>> " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayMessage(String messageToDisplay){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(messageToDisplay);
                    }
                }
        );
    }
}
