package netUtils;

import app.Server;
import concurrent_utils.Channel;
import concurrent_utils.PA;
import concurrent_utils.ThreadPool;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Сергеев on 23.03.2017.
 */
public class Host implements Stoppable  {


    private  ServerSocket serverSocket;
    private  Channel  channel;
    private  MessageHandlerFactory messageHandlerFactory;
    public int sessionCount = 0;//start count users
    private Object lock = new Object();
    private Thread thread;
    public int maxSessionCount;
    private volatile boolean isActive;

    public void closeSession() {
        synchronized (lock) {
            sessionCount--;
            System.out.println("Count of connected users: " + sessionCount + "/" + maxSessionCount);
            lock.notifyAll();
        }
    }

    public void openSession() {
        synchronized (lock) {
            sessionCount++;
            System.out.println("Count of connected users: " + sessionCount + "/" + maxSessionCount);
        }
    }

    @Override
    public void run() {

        while (isActive) {
            Socket socket = null;
            try {

             socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Server: Host stopped");
            }
            MessageHandler messageHandler = messageHandlerFactory.create();
            Session session = new Session(socket, messageHandler, this);
            channel.put(session);
    }



    }
    public void start() {
        thread = new Thread(this);
        thread.start();
        isActive = true;
        System.out.println("Server: Server started");
        System.out.println("Server: Host started");
    }
    public void stop() {

        isActive = false;
        thread.interrupt();
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Server: Host stoped");
            }

    }
    public Host(int port, Channel <Stoppable> channel , MessageHandlerFactory messageHandlerFactory, int maxSessionCount) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.channel = channel;
        this.messageHandlerFactory = messageHandlerFactory;
        this.maxSessionCount=maxSessionCount;
        isActive = true;
    }
}
