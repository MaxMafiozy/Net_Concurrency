import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Сергеев on 13.02.2017.
 */
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            Socket socket = serverSocket.accept();
            InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStream = socket.getOutputStream();// байтовый поток
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            while (true) {
                String message = dataInputStream.readUTF();
                if (message.endsWith("exit")) {
                    dataOutputStream.writeUTF("Server: Connection close");
                    dataOutputStream.flush();
                    socket.close();
                    System.out.println("Client: Connection close");
                    break;
                } else
                    {
                    dataOutputStream.writeUTF("Server: Message \"" + message + "\" was delivered");
                    dataOutputStream.flush();
                    System.out.println("Client: The received message is " + "\"" + message + "\"");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}