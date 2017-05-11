package netUtils;

import java.net.Socket;

/**
 * Created by Сергеев on 09.05.2017.
 */
public interface MessageHandler {
    void handle(Socket socket);
}
