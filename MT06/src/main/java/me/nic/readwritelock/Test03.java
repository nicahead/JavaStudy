package me.nic.readwritelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 演示 ReadWriteLock 的 writeLock()写锁是互斥的,只允许有一个线程持有
 */
public class Test03 {
    static class Service {
        // 先定义读写锁
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        // 定义方法修改数据
        public void write() {
            try {
                // 获得写锁
                readWriteLock.writeLock().lock();
                System.out.println(Thread.currentThread().getName() + "获得写锁，开始修改数据" + System.currentTimeMillis());
                // 模拟读取数据用时
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + "修改数据完毕" + System.currentTimeMillis());
                readWriteLock.writeLock().unlock();
            }
        }
    }

    public static void main(String[] args) {
        Service service = new Service();
        // 创建5个线程，修改数据
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    service.write();
                }
            }).start();
        }
        // 从执行结果来看,同一时间只有一个线程获得写锁
    }
}
