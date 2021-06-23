package me.nic.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock锁的可重入
 */
public class Test04 {

    static class SubThread extends Thread {
        // 定义锁对象
        // 注意这个Lock不能是实例变量，必须是静态变量，否则不同线程就拥有了自己的锁对象，无法实现同步
        private static Lock lock = new ReentrantLock();
        // 定义变量
        public static int num = 0;

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                try {
                    // 可重入锁指可以重复获得该锁
                    lock.lock();
                    lock.lock();
                    num++;
                } finally {
                    // 加锁两次则必须也释放两次
                    lock.unlock();
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SubThread t1 = new SubThread();
        SubThread t2 = new SubThread();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(SubThread.num);
    }
}
//20000
//两个线程各加10000次，如果没有线程同步，则会出现丢失修改，得到小于20000的数