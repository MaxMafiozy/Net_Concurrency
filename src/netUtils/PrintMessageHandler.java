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
    public void handle(String message){
        System.out.println("Received message: "+message);
    }
}
