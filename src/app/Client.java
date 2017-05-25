package app;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


/**
 * Created by Сергеев on 13.02.2017.
 */
public class Client {
    public static void main(String[] args) {
        int port;
        Socket socket;
        String answer;
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader keyboard = new BufferedReader(isr);
            String host = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                System.out.println("Wrong port format. Should be integer");
                return;
            }
            try {
                try {

                    socket = new Socket(host, port);
                    
                } catch (ConnectException e) {
                    System.out.println("Can't connection to this port for this host. This port is wrong");
                    return;
                }
            } catch (UnknownHostException e) {
                System.out.println("Can't connection to this host. May be host format is wrong or not available");
                return;
            }

            InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStream = socket.getOutputStream();

            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            Thread myThready = new Thread(new Runnable()
            {
                public void run() //Этот метод будет выполняться в побочном потоке
                {
                    System.out.println("Привет из побочного потока!");
                    while (true) {
                        try {
                            String answer = dataInputStream.readUTF();

                            if (answer.endsWith("Server: Connection close")) {

                                System.out.println(answer);

                            } else {
                                System.out.println(answer);
                            }
                        } catch (SocketException e) {
                            System.out.println("Connection lost");
                            System.exit(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            myThready.start();
            while (true) {
                try {

                    String message = keyboard.readLine();
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                } catch (SocketException e) {
                    System.out.println("Connection lost");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}