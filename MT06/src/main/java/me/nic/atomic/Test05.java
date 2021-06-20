package me.nic.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * AtomicStampedReference 原子类可以解决CAS中的ABA问题
 * 在 AtomicStampedReference 原子类中有一个整数标记值 stamp, 每次执行 CAS 操作时,需要对比它的版本,即比较 stamp 的值
 */
public class Test05 {

    // 定义A
    private static AtomicStampedReference<String> stampedReference = new AtomicStampedReference<>("abc", 0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                expectedReference – the expected value of the reference
                newReference – the new value for the reference
                expectedStamp – the expected value of the stamp
                newStamp – the new value for the stamp
                 */
                System.out.println(Thread.currentThread().getName() + "--" + stampedReference.getReference() + "--" + stampedReference.getStamp()); // Thread-0--abc--0
                stampedReference.compareAndSet("abc", "def", stampedReference.getStamp(), stampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + "--" + stampedReference.getReference() + "--" + stampedReference.getStamp()); // Thread-0--def--1
                stampedReference.compareAndSet("def", "abc", stampedReference.getStamp(), stampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + "--" + stampedReference.getReference() + "--" + stampedReference.getStamp()); // Thread-0--abc--2
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 在睡眠之前获取stamp
                int stamp = stampedReference.getStamp();
                try {
                    // 睡1秒，等线程t1执行完
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "--" + stampedReference.getReference() + "--" + stamp); // Thread-1--abc--0
                // 这个时候stamp != stampedReference.getStamp(),即版本号已经被修改过了，尽管期望值="abc"，但是期待版本号!=stamp，所以修改失败
                System.out.println(stampedReference.compareAndSet("abc", "ggg", stamp, stamp + 1)); // false
            }
        });

        t1.start();
        t2.start();
        // 等t1、t2执行完
        t1.join();
        t2.join();
        System.out.println(stampedReference.getReference()); // abc
    }
}
