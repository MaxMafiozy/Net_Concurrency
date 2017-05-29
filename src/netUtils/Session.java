package netUtils;

import app.Server;
import concurrent_utils.SocketList;

import java.io.*;
import java.net.Socket;


/**
 * Created by Сергеев on 17.02.2017.
 */
public class Session implements Stoppable {
    MessageHandler messageHandler;
    ChatMessageHandler chatmessageHandler;
    private Socket socket;
    Host host;
    SocketList socketList;
String name;
    @Override
    public void run() {
        try {
            host.openSession();
            try {

                InputStream socketInputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(socketInputStream);
                String messagename = dataInputStream.readUTF();
                if(messagename.startsWith("[Name]")){
                    name=messagename.replace("[Name] ","");
                    socketList.putname(name);
                    chatmessageHandler.handle("[New User]", name,socket);
                }

                while (true) {
                    String message = dataInputStream.readUTF();
                    if (message.endsWith("%exit")) {
                        System.out.println("Client " + name + ": Connection close");
                        break;
                    } else {
                        chatmessageHandler.handle(message, name,socket);
                        messageHandler.handle(message);
                    }
                }

                socket.close();

            } catch (IOException e) {
                System.out.println("Client " + socket + ": Connection close");
            }
        } finally {
            socketList.nametake(socketList.getname(name));
            socketList.take(socketList.getid(socket));
            chatmessageHandler.handle("[Delete User]",name,socket);
            host.closeSession();

        }
    }

    public Session(Socket socket, MessageHandler messageHandler, Host host, SocketList socketList, ChatMessageHandler chatmessageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;
        this.host = host;
        this.socketList = socketList;
        this.chatmessageHandler = chatmessageHandler;
    }

    @Override
    public void stop() {
        if (socket != null) {
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("Server: Connection close");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
