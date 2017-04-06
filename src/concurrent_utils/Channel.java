package concurrent_utils;

import netUtils.Session;

import java.util.LinkedList;

/**
 * Created by Сергеев on 17.03.2017.
 */
public class Channel {
    private int maxCount;
    static final Object lock = new Object();
    private final LinkedList<Runnable> linkedList = new LinkedList();

    Runnable take() {
        synchronized (lock) {
            while (linkedList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notifyAll();
            return linkedList.removeFirst();
        }
    }

    public void put(Runnable session) {
        synchronized (lock) {
            while (maxCount <= linkedList.size()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            linkedList.addLast(session);
            lock.notifyAll();
        }
    }
public int size()


    {
        synchronized (lock)
        {
            return linkedList.size();
        }
    }
    public Channel(int maxCount) {
        this.maxCount = maxCount;
    }


}

