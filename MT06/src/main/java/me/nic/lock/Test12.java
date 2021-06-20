package me.nic.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 Condition 实现生产者/消费者设计模式, 两个线程交替打印
 */
public class Test12 {
    static class MyService {
        // 创建锁对象
        private Lock lock = new ReentrantLock();
        // 创建condition对象
        private Condition condition = lock.newCondition();
        // 定义交替打印标志
        private boolean flag = true;

        // 定义方法只打印--横线
        public void printOne() {
            try {
                // 加锁
                lock.lock();
                while (flag) {
                    System.out.println(Thread.currentThread().getName() + "waiting...");
                    condition.await();
                }
                // 当flag为false时打印
                System.out.println(Thread.currentThread().getName() + "-----------");
                // 修改交替打印标志
                flag = true;
                // 通知另外的线程打印
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        // 定义方法只打印**横线
        public void printTwo() {
            try {
                // 加锁
                lock.lock();
                // 当flag为false时等待
                while (!flag) {
                    System.out.println(Thread.currentThread().getName() + "waiting...");
                    condition.await();
                }
                // 当flag为true时打印
                System.out.println(Thread.currentThread().getName() + "**********");
                // 修改交替打印标志
                flag = false;
                // 通知另外的线程打印
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        MyService myService = new MyService();
        // 创建线程打印----
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    myService.printOne();
                }
            }
        }).start();
        // 创建线程打印****
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    myService.printTwo();
                }
            }
        }).start();
    }
}
