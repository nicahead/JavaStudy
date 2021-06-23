package me.nic.wait;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用wait-notify实现一个Lock-美团面试题目
 */
class MyLock {
    // 当前占有锁的线程id，没有线程占有的时候是-1
    private long owner = -1;
    public synchronized void lock() {
        // 首先判断是否是合法操作，假设锁不可重入
        if (Thread.currentThread().getId() == owner) {
            throw new IllegalStateException("不可重复获取锁");
        }
        // 加锁操作是将当前占有锁的线程变成自己
        // 使用当前锁是否被占用作为判断条件，如果锁被占用，current thread等待
        while (owner != -1) {
            try {
                System.out.println(String.format("thread %s 等待...", Thread.currentThread().getId()));
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 此时锁对象被释放，之前等待的线程可以去竞争获得锁对象
        owner = Thread.currentThread().getId();
        System.out.println(String.format("thread %s 获得锁...", Thread.currentThread().getId()));
    }

    public synchronized void unlock() {
        // 首先判断是否是合法操作，只有持有锁对象的线程才可以释放锁
        if (Thread.currentThread().getId() != owner) {
            throw new IllegalStateException("只有持有锁的线程可以释放锁");
        }
        owner = -1;
        System.out.println(String.format("thread %s 释放锁...", Thread.currentThread().getId()));
        this.notifyAll();
    }
}

public class Test02 {
    public static void main(String[] args) throws Exception {
        MyLock lock = new MyLock();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    System.out.println(String.format("thread %s 正在运行...", Thread.currentThread().getId()));
                    try {
                        // 休眠随机时间，模拟执行任务
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }
    }
}
