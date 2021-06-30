package me.nic.wait;

/**
 * interrupt()结束等待
 */
public class Test03 {

    // 锁对象
    private static final Object lockObj = new Object();

    static class SubThread extends Thread {
        @Override
        public void run() {
            synchronized (lockObj) {
                try {
                    // me.nic.wait
                    System.out.println("开始等待...");
                    lockObj.wait();
                    System.out.println("结束等待...");
                } catch (InterruptedException e) {
                    System.out.println("wait被中断...");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SubThread t = new SubThread();
        t.start();
        // 主线程睡眠3秒，确保子线程进入wait状态
        Thread.sleep(3000);
        t.interrupt();
    }
}
