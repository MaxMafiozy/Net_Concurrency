package netUtils;

import java.io.*;
import java.net.Socket;

/**
 * Created by Сергеев on 31.03.2017.
 */
public class PrintMessageHandler implements MessageHandler {
    @Override
    public void handle(Socket socket) {
        try {
            new DataOutputStream(socket.getOutputStream()).writeUTF("Server: Connection successful");
            InputStream socketInputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            OutputStream socketOutputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);

            while (true) {
                String message = dataInputStream.readUTF();
                if (message.endsWith("exit")) {
                    dataOutputStream.writeUTF("Server: Connection close");
                    dataOutputStream.flush();
                    System.out.println("Client " + socket + ": Connection close");
                    break;
                } else {
                    dataOutputStream.writeUTF("Server: Message \"" + message + "\" was delivered");
                    dataOutputStream.flush();
                    System.out.println("Client " + socket + ": The received message is " + "\"" + message + "\"");
                }
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Client " + socket + ": Connection close");
        }

    }

}
