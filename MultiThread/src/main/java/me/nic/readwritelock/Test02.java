package me.nic.readwritelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock 读写锁可以实现读读共享,允许多个线程同时获得读锁
 */
public class Test02 {
    static class Service {
        // 定义读写锁
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        // 定义方法读取数据
        public void read() {
            try {
                // 获得读锁
                readWriteLock.readLock().lock();
                System.out.println(Thread.currentThread().getName() + "获得读锁，开始读取数据" + System.currentTimeMillis());
                // 模拟读取数据用时
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
    }

    public static void main(String[] args) {
        Service service = new Service();
        // 创建5个线程，调用read()方法
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    service.read();
                }
            }).start();
        }
        // 运行程序后，这多个线程几乎可以同时获得锁读，执行lock后面的代码
    }
}
