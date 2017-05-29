package app;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Сергеев on 14.05.2017.
 */
public class ClientForm {
    private static String host;
    private static int port;
    private JPanel panel1;
    private JTextField textField2;
    private JButton sendButton;
    private JButton connectButton;
    private JTextArea textArea1;
    public JTextArea textArea2;
    private JTextField textField1;
    Socket socket;
    String username;
   


    public ClientForm() {

        connectButton.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent ex) {
                myThready2.start();
                connectButton.setEnabled(false);
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent ex) {
                OutputStream socketOutputStream = null;
                try {
                    socketOutputStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
                try {

                    String message = textField2.getText();
                    textArea1.append("Вы: "+message +"\n");
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                    textField2.setText("");
                } catch (SocketException e) {
                    System.out.println("Connection lost");
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    Thread myThready2 = new Thread(new Runnable() {
        public void run() {

            try {

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


                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("[Name] " + textField1.getText());
                    dataOutputStream.flush();
                    username = textField1.getText();
                    textField1.setEditable(false);
                } catch (IOException e) {
                    e.printStackTrace();

                }
                InputStream socketInputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(socketInputStream);
                Thread myThready = new Thread(new Runnable() {
                    public void run() //Этот метод будет выполняться в побочном потоке
                    {
                        while (true) {
                            try {
                                String answer = dataInputStream.readUTF();
                                if (answer.endsWith("[New User]")) {
                                    textArea2.setText("");
                                    textArea2.append(answer.replace(": [New User]", "") + "\n");
                                } else {
                                    if (answer.endsWith("[Delete User]")) {
                                        System.out.println("Delete");
                                        textArea2.setText("");
                                        textArea2.append(answer.replace(": [Delete User]", "") + "\n");
                                    } else {
                                        textArea1.append(answer + "\n");
                                    }
                                }
                            } catch (IOException e) {
                                System.exit(0);
                                break;
                            }
                        }
                    }
                });
                myThready.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });


    public static void main(String[] args) {
        JFrame frame = new JFrame("Client");
        frame.setContentPane(new ClientForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        host =  args[0];
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.out.println("Wrong port format. Should be integer");
            return;
        }
    }

}
