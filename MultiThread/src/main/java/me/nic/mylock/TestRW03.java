package me.nic.mylock;

import java.util.concurrent.TimeUnit;

/**
 * 测试读写锁读写互斥
 */
public class TestRW03 {
    static class Service {
        // 先定义读写锁
        ReadWriteLock readWriteLock = new ReadWriteLock();
        // 获得读锁
        MyLock04 readLock = readWriteLock.getReadLock();
        // 获得写锁
        MyLock04 writeLock = readWriteLock.getWritelock();

        // 定义方法读取数据
        public void read() {
            try {
                // 获得读锁
                readLock.lock();
                System.out.println(Thread.currentThread().getName() + "获得读锁，开始读取数据" + System.currentTimeMillis());
                // 模拟读取数据用时
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + "读取数据完毕" + System.currentTimeMillis());
                readLock.unlock();
            }
        }

        // 定义方法修改数据
        public void write() {
            try {
                // 获得写锁
                writeLock.lock();
                System.out.println(Thread.currentThread().getName() + "获得写锁，开始修改数据" + System.currentTimeMillis());
                // 模拟读取数据用时
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + "修改数据完毕" + System.currentTimeMillis());
                writeLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Service service = new Service();
        // 定义一个线程读数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.read();
            }
        }).start();
        // 定义一个线程写数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.write();
            }
        }).start();
    }
}


