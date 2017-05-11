package concurrent_utils;

import concurrent_utils.Channel;
import netUtils.Stoppable;

import java.util.LinkedList;

/**
 * Created by Сергеев on 24.03.2017.
 */
public class ThreadPool {
    private final LinkedList <WorkerThread> allWorkers;
    private final Object lock = new Object();
    private final  Channel <WorkerThread> freeWorkers;
    int maxSize;

    public void execute(Stoppable task) {
        synchronized (lock) {
            if ((freeWorkers.size() == 0)&& (allWorkers.size() < maxSize)){

                    WorkerThread thread = new WorkerThread(this);
                    allWorkers.add(thread);
                    freeWorkers.put(thread);

                }
        }
        ((WorkerThread) freeWorkers.take()).execute(task);
    }

    public void onTaskCompleted(WorkerThread workerThread) {
        freeWorkers.put(workerThread);
    }

    public void stop() {
        for(WorkerThread worker : allWorkers)
            worker.stop();
        System.out.println("Server: Threadpool stopped");
    }

    public ThreadPool(int maxSize) {
        WorkerThread workerThread = new WorkerThread(this);
        this.maxSize = maxSize;
        this.freeWorkers = new Channel<>(maxSize);
        this.allWorkers = new LinkedList<>();
        allWorkers.add(workerThread);
        freeWorkers.put(workerThread);
    }
}
