package me.nic.lock;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过 ReentrantLock 锁的 lockInterruptibly()方法避免死锁的产生
 */
public class Test06 {
    static class IntLock implements Runnable {
        // 创建两个ReentrantLock锁对象
        public static ReentrantLock lock1 = new ReentrantLock();
        public static ReentrantLock lock2 = new ReentrantLock();
        // 定义整数变量，决定使用哪个锁
        int lockNum;

        public IntLock(int lockNum) {
            this.lockNum = lockNum;
        }

        @Override
        public void run() {
            try {
                // lockNum是奇数，先锁1，再锁2
                if (lockNum % 2 == 1) {
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + "获得锁1，还需要获得锁2");
                    Thread.sleep(new Random().nextInt(500));
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + "同时获得了锁1和锁2");
                }
                // lockNum是偶数，先锁2，再锁1
                else {
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + "获得锁2，还需要获得锁1");
                    Thread.sleep(new Random().nextInt(500));
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + "同时获得了锁1和锁2");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 判断当前线程是否持有该锁
                if (lock1.isHeldByCurrentThread())
                    lock1.unlock();
                if (lock2.isHeldByCurrentThread())
                    lock2.unlock();
                System.out.println(Thread.currentThread().getName() + "线程退出");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        IntLock intLock1 = new IntLock(11);
        IntLock intLock2 = new IntLock(22);

        Thread t1 = new Thread(intLock1);
        Thread t2 = new Thread(intLock2);
        t1.start();
        t2.start();
        // 这时候出现了死锁，Thread-0获得锁1，还需要获得锁2；Thread-1获得锁2，还需要获得锁1
        // 在 main 线程,等待 3000 秒,如果还有线程没有结束就中断该线程
        Thread.sleep(3000);
        // 可以中断任何一个线程来解决死锁, t2 线程会放弃对锁 1 的申请,同时释放锁 2, t1 线程会完成它的任务
        if (t2.isAlive())
            t2.interrupt();
    }
}
