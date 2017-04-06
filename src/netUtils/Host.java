package netUtils;
import app.Server;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

/**
 * Created by Сергеев on 23.03.2017.
 */
public class Host {
    public static void main(String[] args) {
        int maxSessionCount, port;
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
            System.out.println("app.Server: port " + port + " is alredy used");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread server = new Thread(new Server(serverSocket, port, maxSessionCount));
        server.start();
    }
}
