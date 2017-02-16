import java.io.*;
import java.net.Socket;

/**
 * Created by Сергеев on 13.02.2017.
 */
public class Client {
    public static void main(String[] args) {
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader keyboard = new BufferedReader(isr);
            Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStream = socket.getOutputStream();// байтовый поток
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            while (true) {
                String message = keyboard.readLine();
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                String answer = dataInputStream.readUTF();
                if (answer.endsWith("Server: Connection close")) {

                    System.out.println(answer);
                    break;
                } else
                    {
                    System.out.println(answer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}