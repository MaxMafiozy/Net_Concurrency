package netUtils;

import concurrent_utils.SocketList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Сергеев on 31.03.2017.
 */
public class PrintMessageHandler implements MessageHandler {




    @Override
    public void handle(String message, String name, Socket socket) {
        try{
            switch (message){
                case "[New User]":     String mes="";
                    for (String s : SocketList.nameList) {
                        if(s!=SocketList.nameList.getLast()) {
                            mes = mes + s + "\n";
                        }
                        else {
                            mes = mes + s;
                        }
                    }
                    for (Socket k : SocketList.linkedList) {
                        try {
                            new DataOutputStream(k.getOutputStream()).writeUTF(mes + ": " + message);
                            new DataOutputStream(k.getOutputStream()).writeUTF("Server: User \"" + name + "\" connected");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "[Delete User]":      String mesdel="";
                    for (String k : SocketList.nameList) {
                        if(k!=SocketList.nameList.getLast()) {
                            mesdel = mesdel + k + "\n";
                        }
                        else {
                            mesdel = mesdel + k;
                        }
                    }
                    for (Socket s : SocketList.linkedList) {
                        try {
                            new DataOutputStream(s.getOutputStream()).writeUTF(mesdel+": " + message);
                            new DataOutputStream(s.getOutputStream()).writeUTF("Server: User \"" + name + "\" disconnected");
                        } catch (SocketException se) {
                            System.out.println("Server: \"" + name + "\" deleted");
                        }
                    }
                    break;
                default:     for (Socket s : SocketList.linkedList) {
                    try {
                        if(s!=socket) {
                            new DataOutputStream(s.getOutputStream()).writeUTF(name + ": " + message);
                        }
                    } catch (SocketException se) {
                        System.out.println("Server: \"" + name + "\" deleted");
                    }
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
