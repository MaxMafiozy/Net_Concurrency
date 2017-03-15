import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Сергеев on 13.02.2017.
 */
public class Server {
    public static int sessionCount = 0;//start count users
    static final Object lock =new Object();
    public static void main(String[] args) {

        int maxSessionCount;
        int port;

            try {
                maxSessionCount = Integer.parseInt(args[1]);//max users (default 2)
            } catch (NumberFormatException ex) {
                System.out.println("Wrong maxSessionCount format. Should be integer");
                return;
            }
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                System.out.println("Wrong port format. Should be integer");
                return;
            }
            ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (BindException e) {
            System.out.println("Server: port " + port + " is alredy used");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server: Server started");
        while (true) {
            try {

            Socket socket = serverSocket.accept();


                try {

                   // dataOutputStream.writeUTF("Server: Wait connection");

                    synchronized (lock) {
                        if (sessionCount >= maxSessionCount) {
                            lock.wait();
                        }
                        OutputStream socketOutputStream = socket.getOutputStream();// байтовый поток
                        DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
                        dataOutputStream.writeUTF("Server: Connection confirmed");
                        dataOutputStream.flush();
                        openSession();
                        Thread thread = new Thread(new Session(socket));
                        thread.start();
                    }
            }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSession() {
        synchronized(lock){
            sessionCount--;
            System.out.println("Count of connected users: " + sessionCount);
            lock.notifyAll();
        }
    }
    public static  void openSession() {
        synchronized(lock){
            sessionCount++;
            System.out.println("Count of connected users: " + sessionCount);
        }
    }

}