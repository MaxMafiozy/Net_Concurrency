package concurrent_utils;

import concurrent_utils.ThreadPool;
import javafx.concurrent.Task;

/**
 * Created by Сергеев on 24.03.2017.
 */
public class WorkerThread implements Runnable {
    Thread thread;
    ThreadPool threadPool;
    Runnable currentTask = null;
    private final Object lock = new Object();
    public void run(){
        synchronized (lock) {

        while (true){

                while (currentTask == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                try {
                    currentTask.run();
                }
                catch (RuntimeException e){
                    e.printStackTrace();
                }
                finally {
                    currentTask=null;
                    threadPool.onTaskCompleted(this);
                }
                }

        }
    }
        }
    public WorkerThread(ThreadPool threadPool) {
        thread=new Thread(this);
        this.threadPool=threadPool;
        thread.start();
    }


    public void execute(Runnable task){
        synchronized (lock) {
           currentTask=task;
            lock.notifyAll();
        }

    }
}
