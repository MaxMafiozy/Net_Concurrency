package netUtils;

import concurrent_utils.SocketList;

/**
 * Created by Сергеев on 09.05.2017.
 */
public class PrintMessageHandlerFactory implements MessageHandlerFactory  {

    public MessageHandler create()  {
        PrintMessageHandler printMessageHandler = new PrintMessageHandler();
        return printMessageHandler;
    }


}
