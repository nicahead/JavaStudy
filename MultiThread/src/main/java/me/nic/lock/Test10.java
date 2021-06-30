package me.nic.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 等待与通知
 */
public class Test10 {
    // 定义锁
    static Lock lock = new ReentrantLock();
    // 获得Condition对象
    static Condition condition = lock.newCondition();

    static class SubThread extends Thread {
        @Override
        public void run() {
            try {
                // 在调用await前必须先获得锁
                lock.lock();
                System.out.println("method lock");
                // 等待
                condition.await();
                System.out.println("method await");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
                System.out.println("method unlock");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SubThread t = new SubThread();
        t.start();
        // 子线程启动后，会转入等待状态
        Thread.sleep(3000);
        // 主线程睡眠3秒后，唤醒子线程的等待
        try {
            lock.lock();
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
