package me.nic.container;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 使用BlockingQueue实现生产者-消费者
 */

// 生产者
class Producer implements Runnable {
    BlockingQueue<String> queue;

    Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println("生产者生产一条消息...");
            String msg = Thread.currentThread().getName() + "生产的消息" + System.currentTimeMillis();
            // 如果队列满了，会阻塞当前线程
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// 消费者
class Consumer implements Runnable {
    BlockingQueue<String> queue;

    Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // 如果队列为空，会阻塞当前线程
            String msg = queue.take();
            System.out.println(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ProducerConsumerTest {
    public static void main(String[] args) {
        // 定义一个容量为2的阻塞队列
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        for (int i = 0; i < 5; i++) {
            new Thread(producer).start();
            new Thread(consumer).start();
        }
        System.out.println();
    }
}
