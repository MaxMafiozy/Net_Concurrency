package netUtils;

import java.net.Socket;

/**
 * Created by Сергеев on 26.05.2017.
 */
public interface ChatMessageHandler {
    void handle(String message, String name, Socket socket);
}
