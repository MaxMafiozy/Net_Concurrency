package netUtils;

/**
 * Created by Сергеев on 31.03.2017.
 */
public interface MessageHandlerFactory  {
    MessageHandler create();
ChatMessageHandler createchat();
}
