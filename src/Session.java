import java.io.*;
import java.net.Socket;

/**
 * Created by Сергеев on 17.02.2017.
 */
public class Session implements Runnable {
    Socket socket;
    public static int count = 0;//start count users
    public static int limitusers = 2;//max users
    @Override
    public void run() {
        InputStream socketInputStream = null;
        try {
            OutputStream socketOutputStream = socket.getOutputStream();// байтовый поток
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            if (count < limitusers) {
                socketInputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(socketInputStream);
                count++;
                System.out.println("Count of connected users: " + count);
                dataOutputStream.writeUTF("Server: Connection confrimed");
                dataOutputStream.flush();
                while (true) {
                    String message = dataInputStream.readUTF();
                    if (message.endsWith("exit")) {
                        dataOutputStream.writeUTF("Server: Connection close");
                        dataOutputStream.flush();
                        socket.close();
                        System.out.println("Client " + socket + ": Connection close");
                        count--;
                        System.out.println("Count of connected users: " + count);
                        break;
                    } else {
                        dataOutputStream.writeUTF("Server: Message \"" + message + "\" was delivered");
                        dataOutputStream.flush();
                        System.out.println("Client " + socket + ": The received message is " + "\"" + message + "\"");
                    }
                }
            } else {
                dataOutputStream.writeUTF("Server: Server is full\nServer: Connection close");
                dataOutputStream.flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Session(Socket socket) {
        this.socket = socket;

    }

}
