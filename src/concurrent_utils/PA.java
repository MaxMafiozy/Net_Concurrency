package concurrent_utils;

import concurrent_utils.Channel;
import concurrent_utils.ThreadPool;

/**
 * Created by Сергеев on 17.03.2017.
 */
public class PA implements Runnable {
    public static Channel channel;
    public static ThreadPool threadPool;
    public void run() {
        while (true) {
            threadPool.execute(channel.take());
        }
    }

    public PA(Channel channel, ThreadPool threadPool) {
        this.channel = channel;
        this.threadPool=threadPool;
    }
}