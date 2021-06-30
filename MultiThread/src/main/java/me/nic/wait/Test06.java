package me.nic.wait;

/**
 * notify()通知过早
 */
public class Test06 {
    public static void main(String[] args) {
        Object obj = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    try {
                        System.out.println("start me.nic.wait");
                        obj.wait();
                        System.out.println("end me.nic.wait");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    System.out.println("start notify");
                    obj.notify();
                    System.out.println("end notify");
                }
            }
        });
        // 先开启t1再开启t2，大多数情况下，先执行t1的等待，然后由t2把t1唤醒
        // t1.start();
        // t2.start();
        /*
        start me.nic.wait
        start notify
        end notify
        end me.nic.wait
        */

        // 如果先开启t2再开启t1，可能会出现t1线程没有收到通知的情况
        t2.start();
        t1.start();
        /*
        start notify
        end notify
        start me.nic.wait
         */
    }
}
