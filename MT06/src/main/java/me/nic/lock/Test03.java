package me.nic.lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 Lock 锁同步不同方法中的同步代码块
 */
public class Test03 {

    // 定义锁对象
    static Lock lock = new ReentrantLock();

    // 方法1
    public static void sm1() {
        try {
            // 获得锁
            lock.lock();
            // 执行业务逻辑
            System.out.println(Thread.currentThread().getName() + "--method 1 start--" + System.currentTimeMillis());
            Thread.sleep(new Random().nextInt(1000));
            System.out.println(Thread.currentThread().getName() + "--method 1 end--" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public static void sm2() {
        try {
            // 获得锁
            lock.lock();
            // 执行业务逻辑
            System.out.println(Thread.currentThread().getName() + "--method 2 start--" + System.currentTimeMillis());
            Thread.sleep(new Random().nextInt(1000));
            System.out.println(Thread.currentThread().getName() + "--method 2 end--" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                sm1();
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                sm2();
            }
        };
        // 每个方法开启3个线程
        new Thread(r1).start();
        new Thread(r1).start();
        new Thread(r1).start();

        new Thread(r2).start();
        new Thread(r2).start();
        new Thread(r2).start();
    }
}
