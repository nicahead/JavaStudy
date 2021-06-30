package me.nic.wait;

/**
 * notify和notifyAll
 */
public class Test04 {

    static class SubThread extends Thread {
        // 定义实例变量作为锁对象
        private Object lockObj;

        SubThread(Object lockObj) {
            this.lockObj = lockObj;
        }

        @Override
        public void run() {
            synchronized (lockObj) {
                try {
                    // me.nic.wait
                    System.out.println("thread" + Thread.currentThread().getId() + "开始等待...");
                    lockObj.wait();
                    System.out.println("thread" + Thread.currentThread().getId() + "结束等待...");
                } catch (InterruptedException e) {
                    System.out.println("thread" + Thread.currentThread().getId() + "wait被中断...");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 定义一个对象作为子线程的锁对象
        Object obj = new Object();
        SubThread t1 = new SubThread(obj);
        SubThread t2 = new SubThread(obj);
        SubThread t3 = new SubThread(obj);
        t1.start();
        t2.start();
        t3.start();
        // 主线程睡眠3秒，确保子线程进入wait状态
        Thread.sleep(3000);
        // 调用notify唤醒子进程
        synchronized (obj) {
            // 调用一次notify只能唤醒其中的一个线程，其他等待的线程仍然处于等待状态
            // 对于处于等待状态的线程来说，错过了通知信号，这种现象也称为信号丢失
//            obj.notify();
            // 唤醒所以线程
            obj.notifyAll();
        }
    }
}
