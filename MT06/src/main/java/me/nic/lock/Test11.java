package me.nic.lock;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多个 Condition 实现通知部分线程, 使用更灵活
 */
public class Test11 {
    static class Service {
        // 定义锁对象
        private ReentrantLock lock = new ReentrantLock();
        // 定义两个Condition对象
        private Condition conditionA = lock.newCondition();
        private Condition conditionB = lock.newCondition();

        // 定义方法，使用conditionA等待
        public void waitMethodA() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " begin wait A " + System.currentTimeMillis());
                conditionA.await();  // 等待
                System.out.println(Thread.currentThread().getName() + " end wait A " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        // 定义方法，使用conditionB等待
        public void waitMethodB() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " begin wait B " + System.currentTimeMillis());
                conditionB.await();  // 等待
                System.out.println(Thread.currentThread().getName() + " end wait B " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        // 定义方法唤醒conditionA对象上的等待
        public void signalA() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " begin signal A " + System.currentTimeMillis());
                conditionA.signal();
                System.out.println(Thread.currentThread().getName() + " end signal A " + System.currentTimeMillis());
            } finally {
                lock.unlock();
            }
        }

        // 定义方法唤醒conditionB对象上的等待
        public void signalB() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " begin signal B " + System.currentTimeMillis());
                conditionB.signal();
                System.out.println(Thread.currentThread().getName() + " end signal B " + System.currentTimeMillis());
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();
        // 开启两个线程，分别调用waitMethodA、waitMethodB方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.waitMethodA();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.waitMethodB();
            }
        }).start();
        // main线程睡眠3秒
        Thread.sleep(3000);
        // 唤醒conditionA对象上的等待，conditionB上仍然继续等待
        service.signalA();
//        service.signalB();
    }
}
