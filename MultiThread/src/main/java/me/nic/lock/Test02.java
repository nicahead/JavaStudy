package me.nic.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock 锁的基本使用
 */
public class Test02 {

    // 定义锁
    static Lock lock = new ReentrantLock();

    // 定义方法
    public static void sm() {
        // 获得锁
        lock.lock();
        // 这里是同步代码块
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + "--" + i);
        }
        // 释放锁
        lock.unlock();
    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sm();
            }
        };
        // 启动三个线程
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
