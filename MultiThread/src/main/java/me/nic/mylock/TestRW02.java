package me.nic.mylock;

import java.util.concurrent.TimeUnit;

/**
 * 测试读写锁写写互斥
 */
public class TestRW02 {
    static class Service {
        // 先定义读写锁
        ReadWriteLock readWriteLock = new ReadWriteLock();

        // 定义方法修改数据
        public void write() {
            try {
                // 获得写锁
                readWriteLock.getWritelock().lock();
                System.out.println(Thread.currentThread().getName() + "获得写锁，开始修改数据" + System.currentTimeMillis());
                // 模拟读取数据用时
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + "修改数据完毕" + System.currentTimeMillis());
                readWriteLock.getWritelock().unlock();
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
