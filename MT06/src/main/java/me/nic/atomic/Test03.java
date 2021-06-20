package me.nic.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 在多线程中使用 AtomicIntegerArray 原子数组
 */
public class Test03 {
    // 定义原子数组
    static AtomicIntegerArray array = new AtomicIntegerArray(10);

    // 定义一个线程类，在线程类中修改原子数组
    static class AddThread extends Thread {
        @Override
        public void run() {
            // 把原子数组的每个元素自增1000次
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < array.length(); j++) {
                    array.getAndIncrement(j % array.length());
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 定义线程数组
        Thread[] threads = new Thread[10];
        // 创建线程
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new AddThread();
        }
        // 开启子线程
        for (Thread thread : threads) {
            thread.start();
        }
        // 在主线程中查看自增完以后原子数组中的各个元素的值，需要在所有子线程都执行完后再查看
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(array);
    }
}
