package me.nic.lock;

/**
 * 演示锁的可重入
 */
public class Test01 {
    public synchronized void sm1() {
        System.out.println("同步方法1执行");
        /*
        线程执行sm1方法，当前对象this作为锁对象，在sm1中调用sm2，注意当前线程还是持有this锁对象的
        sm2同步方法默认的锁对象也是this对象，要执行sm2必须先获得this锁对象
        当前this对象被当前线程持有，可以再次获得this对象，这就是锁的可重入性，假设锁不可重入的话，可能会造成死锁
         */
        sm2();
    }

    private synchronized void sm2() {
        System.out.println("同步方法2执行");
        sm3();
    }

    private synchronized void sm3() {
        System.out.println("同步方法2执行");
    }

    public static void main(String[] args) {
        Test01 test01 = new Test01();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                test01.sm1();
            }
        });
        thread.start();
        /*
            同步方法1执行
            同步方法2执行
            同步方法2执行
         */
    }
}
