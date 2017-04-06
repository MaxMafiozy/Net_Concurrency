package concurrent_utils;

import concurrent_utils.Channel;

import java.util.LinkedList;

/**
 * Created by Сергеев on 24.03.2017.
 */
public class ThreadPool {
    private  LinkedList allWorkers;
    private final Object lock = new Object();
    private  Channel freeWorkers;
    int maxSize;

    public void execute(Runnable task) {
        synchronized (lock) {
            if (freeWorkers.size() == 0) {
                if (allWorkers.size() < maxSize) {
                    WorkerThread thread = new WorkerThread(this);
                    allWorkers.add(thread);
                    freeWorkers.put(thread);
                }
            }
        }
        freeWorkers.take().execute(task);
    }

    public void onTaskCompleted(WorkerThread workerThread) {
        freeWorkers.put(workerThread);
    }


    public ThreadPool(int maxSize) {
        WorkerThread workerThread = new WorkerThread(this);
        this.maxSize = maxSize;
        this.freeWorkers = new Channel(maxSize);
        this.allWorkers = new LinkedList();
        allWorkers.add(workerThread);
        freeWorkers.put(workerThread);
    }
}
