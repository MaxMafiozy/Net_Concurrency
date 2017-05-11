package netUtils;

import app.Server;

import java.io.*;
import java.net.Socket;


/**
 * Created by Сергеев on 17.02.2017.
 */
public class Session implements Stoppable  {
    MessageHandler messageHandler;
    private Socket socket;
    Host host;


    @Override
    public void run() {
        try {
            host.openSession();
            messageHandler.handle(socket);
        }

        finally {
host.closeSession();
        }
    }

    public Session(Socket socket,  MessageHandler messageHandler, Host host) {
        this.socket = socket;
        this.messageHandler = messageHandler;
        this.host=host;
    }

    @Override
    public void stop() {
        if (socket != null) {
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("Server: Server closed");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
