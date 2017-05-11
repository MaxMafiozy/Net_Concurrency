package concurrent_utils;

import netUtils.Stoppable;

import java.util.LinkedList;

/**
 * Created by Сергеев on 17.03.2017.
 */
public class Channel<T> {
    private final int maxCount;
    private final static Object lock = new Object();
    private final LinkedList<T> linkedList = new LinkedList<>();

    public  T take() {
        synchronized (lock) {
            while (linkedList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            lock.notifyAll();
            return (T) linkedList.removeFirst();
        }
    }

    public void put(T x) {
        synchronized (lock) {
            while (maxCount == linkedList.size()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            linkedList.addLast((T) x);
            lock.notifyAll();
        }
    }


    int size() {
        synchronized (lock) {
            return linkedList.size();
        }
    }

    public Channel(int maxCount) {
        this.maxCount = maxCount;

    }



}

