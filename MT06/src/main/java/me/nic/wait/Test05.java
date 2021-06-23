package me.nic.wait;

public class Test05 {
    public static void main(String[] args) {
        Object obj = new Object();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    try {
                        System.out.println("start me.nic.wait");
                        // 5000毫秒内没有被唤醒，则自动唤醒
                        obj.wait(5000);
                        System.out.println("end me.nic.wait");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
}
