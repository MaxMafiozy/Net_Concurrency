package concurrent_utils;
import netUtils.Stoppable;
/**
 * Created by Сергеев on 24.03.2017.
 */
public class WorkerThread implements Runnable {
    private Thread thread;
    private ThreadPool threadPool;
    private Stoppable currentTask;
    private volatile boolean isActive;
    private final Object lock = new Object();

    public void run() {
        synchronized (lock) {

            while (isActive) {

                while (currentTask == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        if(!isActive){
                            return;
                        }
                    }
                }
                try {
                    currentTask.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    currentTask = null;
                    threadPool.onTaskCompleted(this);
                }
            }
        }
    }

    public WorkerThread(ThreadPool threadPool) {
        thread = new Thread(this);
        this.threadPool = threadPool;
        this.isActive = true;
        thread.start();
    }

    public void stop() {
        isActive = false;
        thread.interrupt();
        if (currentTask != null){
             currentTask.stop();
        }
    }

    public void execute(Stoppable task) {
        synchronized (lock) {
            if (currentTask != null) {
                new IllegalStateException();
            }
            currentTask = task;
            lock.notifyAll();

        }

    }
}
