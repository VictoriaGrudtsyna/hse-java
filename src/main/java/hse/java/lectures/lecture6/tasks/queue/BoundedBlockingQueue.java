package hse.java.lectures.lecture6.tasks.queue;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBlockingQueue<T> {

    private int capacity;
    private Queue<T> queue;
    private int size;

    public BoundedBlockingQueue(int capacity) {
        if (capacity > 0) {
            this.queue = new LinkedList<>();
            this.capacity = capacity;
        }
        else {
            throw new IllegalArgumentException("capacity should be positive!");
        }
    }

    public void put(T item) throws InterruptedException {
        if (item == null) {
            throw new IllegalArgumentException("item should be not null!");
        }
        synchronized(queue) {
            while (queue.size() >= capacity) {
                queue.wait();
            }
            queue.add(item);
            queue.notifyAll();
        }
    }

    public T take() throws InterruptedException {
        synchronized (queue) {
            while (queue.size() == 0) {
                queue.wait();
            }
            T takenElem = queue.poll();
            queue.notifyAll();
            return takenElem;
        }
    }

    public synchronized int size() {
        return queue.size();
    }

    public int capacity() {
        return capacity;
    }
}
