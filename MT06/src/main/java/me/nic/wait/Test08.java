package me.nic.wait;

/**
 * ThreadLocal的基本使用
 */
public class Test08 {
    static ThreadLocal threadLocal = new ThreadLocal();

    static class SubThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                // 设置线程关联的值
                threadLocal.set(Thread.currentThread().getName() + "-" + i);
                // 调用get方法读取关联的值
                System.out.println(Thread.currentThread().getName() + " value= " + threadLocal.get());
            }
        }
    }

    public static void main(String[] args) {
        SubThread t1 = new SubThread();
        SubThread t2 = new SubThread();
        t1.start();
        t2.start();
        /*
        Thread-0 value= Thread-0-0
        Thread-1 value= Thread-1-0
        Thread-0 value= Thread-0-1
        Thread-1 value= Thread-1-1
        ...
         */
    }
}
