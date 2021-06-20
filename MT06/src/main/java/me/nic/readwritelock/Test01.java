package me.nic.readwritelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁基本使用
 */
public class Test01 {
    public static void main(String[] args) {
        // 定义读写锁
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        // 获得读锁
        Lock readLock = rwLock.readLock();
        // 获得写锁
        Lock writeLock = rwLock.writeLock();

        // 读数据
        // 申请读锁
        readLock.lock();
        try {
            System.out.println("读取共享数据");
        } finally {
            readLock.unlock();
        }

        // 写数据
        // 申请写锁
        writeLock.lock();
        try {
            System.out.println("更新修改共享数据");
        } finally {
            writeLock.unlock();
        }
    }
}
