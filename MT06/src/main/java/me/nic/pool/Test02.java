package me.nic.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的计划任务
 */
public class Test02 {
    public static void main(String[] args) {
        // 创建一个有调度功能的线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

        // 1) 延迟2秒后执行任务
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId() + "----延迟任务-- - " + System.currentTimeMillis());
            }
        }, 2, TimeUnit.SECONDS);

        // 2) 以固定频率执行任务，开启任务的时间是固定的，在3秒后执行任务，以后每隔2秒重新执行一次
        // 如果任务执行时长超过了时间间隔, 则任务完成后立即开启下个任务
        // 也就是说这里打印出任务结束之后会立即执行下个任务，不会停顿2秒
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId() + "----在固定频率开启任务-- - " + System.currentTimeMillis());
                try {
                    // 睡眠模拟任务执行时间
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("任务结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 3, 2, TimeUnit.SECONDS);

        // 3) 和上面方法的区别在于，无论任务执行用了多久，delay的时间一定会等
        // 此处打印出任务结束后会等待2秒再去执行任务
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId() + "----在固定频率开启任务-- - " + System.currentTimeMillis());
                // 睡眠模拟任务执行时间,如果任务执行时长超过了时间间隔, 则任务完成后立即开启下个任务
                try {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("任务结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 3, 2, TimeUnit.SECONDS);
    }
}
