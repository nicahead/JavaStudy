package me.nic.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lockInterruptibly() 方法的作用:
 * 如果当前线程未被中断则获得锁,如果当前线程被中断则出现异常
 */
public class Test05 {
    static class Servier {
        private Lock lock = new ReentrantLock();
        public void servierMethod(){
            try {
                // 如果使用lock方法，获得锁对象，即使调用了线程的interrupt()方法，也没有真正地中断线程
                // lock.lock();
                // 使用lockInterruptibly方法，如果线程被中断了，不会获得锁，会产生异常
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName()+" -- begin lock");
                // 执行一段耗时的操作
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    new StringBuffer();
                }
                System.out.println(Thread.currentThread().getName()+" -- end lock");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName()+" -- release lock");
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Servier s = new Servier();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                s.servierMethod();
            }
        };
        Thread t1 = new Thread(r);
        t1.start();
        Thread.sleep(50);

        Thread t2 = new Thread(r);
        t2.start();
        Thread.sleep(50);
        // 中断Thread-1
        t2.interrupt();
    }
}
