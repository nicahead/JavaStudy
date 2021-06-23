package me.nic.wait;

public class Test01 {
    public static void main(String[] args) throws InterruptedException {
        // 定义一个字符串作为锁对象
        String lock = "lock";
        // 定义第一个线程
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 同步代码块
                synchronized (lock) {
                    System.out.println("线程1开始等待：" + System.currentTimeMillis());
                    try {
                        // 当前线程等待，会释放锁对象，当前线程进入阻塞状态
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程1结束等待：" + System.currentTimeMillis());
                }
            }
        });
        // 定义第二个线程，在第二个线程中唤醒第一个线程
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("线程2开始唤醒：" + System.currentTimeMillis());
                    // 唤醒在lock锁对象上等待的某一个线程
                    lock.notify();
                    System.out.println("线程2结束唤醒：" + System.currentTimeMillis());
                }
            }
        });
        // 开启t1线程，t1线程等待
        t1.start();
        // main线程睡眠3秒，确保t1入睡
        Thread.sleep(3000);
        // t1线程开启3秒后，再开启t2线程唤醒t1线程
        t2.start();
    }
}
