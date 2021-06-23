package me.nic.container;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现一个ArrayBlockingQueue
 */
public class MyBlockingQueue<T> {
    // 有界队列内部固定长度,底层使用obj数组存储
    private Object[] data;
    // 定义两个指针，指向队列的头部和尾部
    private int head = 0, tail = 0;
    // 记录队列中元素个数
    private int size;
    // 定义锁和条件
    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    // 实例化时确定队列容量，且不能改变
    MyBlockingQueue(int capacity) {
        this.data = new Object[capacity];
    }

    // 添加数据，若队列已满则阻塞
    public void put(T item) {
        // 加锁
        lock.lock();
        try {
            while (size == data.length) {
                notFull.await();
            }
            // 被唤醒,加入该元素
            data[tail] = item;
            // 移动指针,如果超出数组，则从头开始
            if (++tail == data.length) tail = 0;
            size++;
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 获取数据，若队列为空则阻塞
    public T take() {
        // 加锁
        lock.lock();
        T item = null;
        try {
            while (size == 0) {
                notEmpty.await();
            }
            // 被唤醒,弹出该元素
            item = (T) data[head];
            data[head] = null;
            // 移动指针,如果超出数组，则从头开始
            if (++head == data.length) head = 0;
            size--;
            notFull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return item;
    }
}

// 生产者
class MyProducer implements Runnable {
    MyBlockingQueue<String> queue;

    MyProducer(MyBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("生产者生产一条消息...");
        String msg = Thread.currentThread().getName() + "生产的消息" + System.currentTimeMillis();
        // 如果队列满了，会阻塞当前线程
        queue.put(msg);
    }
}

// 消费者
class MyConsumer implements Runnable {
    MyBlockingQueue<String> queue;

    MyConsumer(MyBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        // 如果队列为空，会阻塞当前线程
        String msg = queue.take();
        System.out.println(msg);
    }
}

class MyProducerConsumerTest {
    public static void main(String[] args) {
        // 定义一个容量为2的阻塞队列
        MyBlockingQueue<String> queue = new MyBlockingQueue<>(2);
        MyProducer producer = new MyProducer(queue);
        MyConsumer consumer = new MyConsumer(queue);
        for (int i = 0; i < 5; i++) {
            new Thread(producer).start();
//            new Thread(consumer).start();
        }
    }
}
