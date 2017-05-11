package concurrent_utils;

import netUtils.Session;
import netUtils.Stoppable;

/**
 * Created by Сергеев on 17.03.2017.
 */
public class PA implements Stoppable {
    private final Channel<Stoppable> channel;
    private final ThreadPool threadPool;
    private volatile boolean isActive;
    private Thread thread;
    public PA (Channel<Stoppable> channel, ThreadPool threadPool) {
        this.channel = channel;
        this.threadPool = threadPool;
        isActive = true;
    }

    public void run() {
        while (isActive)
            threadPool.execute(channel.take());
    }

    @Override
    public void stop() {
        if (isActive) {
            isActive = false;
            threadPool.stop();
            while (channel.size() > 0)
                    channel.take().stop();
            thread.interrupt();
            System.out.println("Server: Dispatcher stopped");
        }
    }
    public void start() {
        thread = new Thread(this);
        isActive = true;
        thread.start();
        System.out.println("Server: Dispatcher started");
    }
}