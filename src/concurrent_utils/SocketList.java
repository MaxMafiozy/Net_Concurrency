package concurrent_utils;

import app.ClientForm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

/**
 * Created by Сергеев on 14.05.2017.
 */
public class SocketList {
    private final int maxCount;
    public static LinkedList<Socket> linkedList = new LinkedList<Socket>();
    public static LinkedList<String> nameList = new LinkedList<String>();
    private final static Object lock = new Object();
    public  Socket take(int socketid) {
        synchronized (lock) {
            while (linkedList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            lock.notifyAll();

            return  linkedList.remove(socketid);
        }
    }
    public void put(Socket x) {
        synchronized (lock) {
            while (maxCount == linkedList.size()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            linkedList.addLast(x);
            lock.notifyAll();
           // send("[New User]", x);
        }
    }
    public int size() {
        synchronized (lock) {
            return linkedList.size();
        }
    }

    public void putname(String x) {
        synchronized (lock) {
            while (maxCount == nameList.size()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nameList.addLast(x);
            lock.notifyAll();
        }
    }
    public  String nametake(int socketid) {
        synchronized (lock) {
            while (linkedList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notifyAll();
            return nameList.remove(socketid);
        }
    }

    public int getid(Socket socket){
        return linkedList.indexOf(socket);
    }
    public int getname(String name){
        return nameList.indexOf(name);
    }
    public SocketList(int maxCount) {
        this.maxCount = maxCount;
    }
}
