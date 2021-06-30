package me.nic.lock;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 tryLock()可以避免死锁
 */
public class Test09 {
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
            // lockNum是奇数，先锁1，再锁2
            if (lockNum % 2 == 1) {
                while (true) {
                    try {
                        if (lock1.tryLock()) {
                            System.out.println(Thread.currentThread().getName() + "获得锁1，还需要获得锁2");
                            Thread.sleep(new Random().nextInt(100));
                            try {
                                if (lock2.tryLock()) {
                                    System.out.println(Thread.currentThread().getName() + "同时获得了锁1和锁2");
                                    // 结束run方法，即当前线程结束
                                    return;
                                }
                            } finally {
                                if (lock2.isHeldByCurrentThread()) {
                                    lock2.unlock();
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (lock1.isHeldByCurrentThread()) {
                            lock1.unlock();
                        }
                    }
                }
            }
            // lockNum是偶数，先锁2，再锁1
            else {
                while (true) {
                    try {
                        if (lock2.tryLock()) {
                            System.out.println(Thread.currentThread().getName() + "获得锁2，还需要获得锁1");
                            Thread.sleep(new Random().nextInt(100));
                            try {
                                if (lock1.tryLock()) {
                                    System.out.println(Thread.currentThread().getName() + "同时获得了锁1和锁2");
                                    // 结束run方法，即当前线程结束
                                    return;
                                }
                            } finally {
                                if (lock1.isHeldByCurrentThread()) {
                                    lock1.unlock();
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (lock2.isHeldByCurrentThread()) {
                            lock2.unlock();
                        }
                    }
                }
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

        // 运行后，使用tryLock尝试获得锁，不会傻傻地等待
        // 通过循环不停地再次尝试，如果等待的时间足够长，线程总是会获得想要的资源
    }
}
