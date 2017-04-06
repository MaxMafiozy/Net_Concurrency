package netUtils;

import app.Server;

import java.io.*;
import java.net.Socket;


/**
 * Created by Сергеев on 17.02.2017.
 */
public class Session implements Runnable {
    Socket socket;
    Server server;

    @Override
    public void run() {
        try {
            InputStream socketInputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            OutputStream socketOutputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            dataOutputStream.flush();
            while (true) {
                String message = dataInputStream.readUTF();
                if (message.endsWith("exit")) {
                    dataOutputStream.writeUTF("app.Server: Connection close");
                    dataOutputStream.flush();
                    System.out.println("app.Client " + socket + ": Connection close");
                    break;
                } else {
                    dataOutputStream.writeUTF("app.Server: Message \"" + message + "\" was delivered");
                    dataOutputStream.flush();
                    System.out.println("app.Client " + socket + ": The received message is " + "\"" + message + "\"");
                }
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("app.Client " + socket + ": Connection close");
        } finally {
            server.closeSession();
        }
    }

    public  Session(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

}
