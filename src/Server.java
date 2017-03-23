import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Сергеев on 13.02.2017.
 */
public class Server implements Runnable {
    public volatile  int sessionCount = 0;//start count users
    private volatile int maxSessionCount;
    static  Object lock =new Object();
    private int port;
    private ServerSocket serverSocket;

    public Server (ServerSocket serverSocket, int port, int maxSessionCount){
                this.serverSocket = serverSocket;
                this.port = port;
              this.maxSessionCount = maxSessionCount;
    }



    public  void closeSession() {
        synchronized(lock){
            sessionCount--;
            System.out.println("Count of connected users: " + sessionCount);
            lock.notifyAll();
        }
    }
    public   void openSession() {
        synchronized(lock){
            sessionCount++;
            System.out.println("Count of connected users: " + sessionCount);
        }
    }
    public void run() {
        System.out.println("Server: Server started");
        Channel channel=new Channel(maxSessionCount);
        PA pa=new PA(channel);
        while (true) {
            try {

                Socket socket = serverSocket.accept();






                synchronized (lock) {
                    while (sessionCount == maxSessionCount) {
                        try {
                            lock.wait();
                        }
                        catch (InterruptedException e) {
                            System.out.println("Some errors");
                        }
                    }

                    OutputStream socketOutputStream = socket.getOutputStream();// байтовый поток
                    DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
                    dataOutputStream.writeUTF("Server: Connection confirmed");
                    Session session=new Session(socket, Server.this);

                    channel.put(session);
                    Thread thread = new Thread(pa);
                    thread.start();
                    dataOutputStream.flush();

                    openSession();

                }



            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}