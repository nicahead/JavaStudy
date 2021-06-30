package me.nic.mylock;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用wait-notify实现一个Lock
 * 排队，公平锁
 * 无法实现？阻塞线程排队，但是锁被释放之后无法选择性通知来唤醒队首线程
 */
class MyLock03 {

    // 当前占有锁的线程id，没有线程占有的时候是-1
    private long owner = -1;
    // 锁计数器
    private int count = 0;

    public synchronized void lock() {
        // 加锁操作是将当前占有锁的线程变成自己
        // 使用当前锁是否被占用作为判断条件，如果锁被占用（且不是当前线程占用），current thread等待
        while (owner != -1 && Thread.currentThread().getId() != owner) {
            try {
                System.out.printf("thread %s 等待...%n", Thread.currentThread().getId());
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 此时锁对象被释放，之前等待的线程可以去竞争获得锁对象
        owner = Thread.currentThread().getId();
        count++;
        System.out.printf("thread %s 获得锁...%n", Thread.currentThread().getId());
    }

    public synchronized void unlock() {
        // 首先判断是否是合法操作，只有持有锁对象的线程才可以释放锁
        if (Thread.currentThread().getId() != owner) {
            throw new IllegalStateException("只有持有锁的线程可以释放锁");
        }
        count--;
        if (count == 0) {
            // 锁对象的占有设为null
            owner = -1;
        }
        System.out.printf("thread %s 释放锁...%n", Thread.currentThread().getId());
        this.notifyAll();
    }

    public void printMsg() {
        System.out.println("owner = " + owner + ";count = " + count);
    }
}

public class Test03 {
    public static void main(String[] args) throws Exception {
        MyLock03 lock = new MyLock03();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                lock.lock();
                System.out.println(String.format("thread %s 正在运行...", Thread.currentThread().getId()));
                try {
                    // 休眠随机时间，模拟执行任务
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.printMsg();
                    lock.unlock();
                    lock.printMsg();
                    lock.unlock();
                    lock.printMsg();
                }
            }
        });
    }
}
