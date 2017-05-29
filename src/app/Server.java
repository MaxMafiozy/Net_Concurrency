package app;


import concurrent_utils.Channel;
import concurrent_utils.PA;
import concurrent_utils.SocketList;
import concurrent_utils.ThreadPool;
import netUtils.Host;
import netUtils.MessageHandlerFactory;
import netUtils.Stoppable;

import java.net.Socket;


/**
 * Created by Сергеев on 13.02.2017.
 */
public class Server  {
    public static void main(String[] args) {
        Class classMessageHandlerFactory = null;
        MessageHandlerFactory messageHandlerFactory = null;
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
        try {
                        classMessageHandlerFactory = Class.forName("netUtils.PrintMessageHandlerFactory");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                 }
        try {
                       messageHandlerFactory = (MessageHandlerFactory) classMessageHandlerFactory.newInstance();
                   } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                      e.printStackTrace();
                }
        Channel<Stoppable> channel = new Channel(maxSessionCount);
        SocketList socketList=new SocketList(maxSessionCount);
        Host host = new Host(port, channel, messageHandlerFactory,maxSessionCount, socketList)  ;
        host.start();
        ThreadPool threadPool = new ThreadPool(maxSessionCount);
        PA dispatcher = new PA(channel, threadPool);




        dispatcher.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                host.stop();
                dispatcher.stop();
                threadPool.stop();


            }
        }));
    }
}