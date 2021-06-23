package me.nic.wait;

import java.util.Date;

/**
 * ThreadLocal初始值
 * 定义ThreadLocal类的子类，在子类中重写initialValue方法指定初始值
 * 这样第一次调用get方法就不会返回null
 */
public class Test10 {
    // 定义ThreadLocal的子类
    static class SubThreadLocal extends ThreadLocal<Date> {
        // 重写initialValue方法，设置初始值
        @Override
        protected Date initialValue() {
            return new Date();
        }
    }

    // 使用自定义的ThreadLocal对象
    static SubThreadLocal threadLocal = new SubThreadLocal();

    static class SubThread extends Thread {
        @Override
        public void run() {
            // 在threadLocal没有set的情况下去get
            // 由于重写了initialValue方法，所以取出的是当前时间
            System.out.println(Thread.currentThread().getName() + " value = " + threadLocal.get());
        }
    }

    public static void main(String[] args) {
        SubThread t = new SubThread();
        t.start();
    }
}
