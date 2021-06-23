package me.nic.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * tryLock(long time, TimeUnit unit) 的基本使用
 */
public class Test07 {
    static class TimeLock implements Runnable {
        // 定义锁对象
        private static ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            try {
                // 3秒内获得锁则返回true
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    System.out.println(Thread.currentThread().getName() + "获得锁，执行耗时任务");
                    /*
                    假设Thread-0线程先持有锁，完成任务需要4秒钟
                    Thread-1线程尝试获得锁，Thread-1线程在3秒内还没有获得锁的话，则会放弃
                    Thread-0获得锁，执行耗时任务
                    Thread-1没有获得锁
                     */
                    Thread.sleep(4000);

                    /*
                    假设Thread-0线程先持有锁，完成任务需要2秒钟
                    Thread-1线程尝试获得锁，3秒内可以获得锁对象
                    Thread-0获得锁，执行耗时任务
                    Thread-1获得锁，执行耗时任务
                     */
                    //Thread.sleep(2000);
                }
                // 没有获得锁
                else {
                    System.out.println(Thread.currentThread().getName() + "没有获得锁");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        TimeLock timeLock = new TimeLock();
        Thread t1 = new Thread(timeLock);
        Thread t2 = new Thread(timeLock);
        t1.start();
        t2.start();
    }
}
