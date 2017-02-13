import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Сергеев on 13.02.2017.
 */
public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",2500); // подключение к серверу
            OutputStream socketOutputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            dataOutputStream.writeUTF("Hello ");
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}