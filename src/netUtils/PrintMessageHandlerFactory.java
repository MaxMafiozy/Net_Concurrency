package netUtils;

import concurrent_utils.SocketList;

/**
 * Created by Сергеев on 09.05.2017.
 */
public class PrintMessageHandlerFactory implements MessageHandlerFactory  {


    public MessageHandler create()  {
        PrintMessageHandler MessageHandler = new PrintMessageHandler();
        return MessageHandler;
    }
    public ChatMessageHandler createchat()  {
        PrintChatMessageHandler ChatMessageHandler = new PrintChatMessageHandler();
        return ChatMessageHandler;
    }
}
