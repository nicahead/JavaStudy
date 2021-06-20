package me.nic.atomic;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 使用原子变量类定义一个计数器
 * 该计数器,在整个程序中都能使用,并且所有的地方都使用这一个计数器,这个计数器可以设计为单例
 */
class Indicator {
    // 构造方法私有化
    private Indicator() {

    }

    // 定义一个私有的本类静态的对象
    private static final Indicator INDICATOR = new Indicator();

    // 提供一个公共静态方法返回该类唯一实例
    public static Indicator getInstance() {
        return INDICATOR;
    }

    // 使用原子变量类保存请求总数、成功数、失败数
    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong successCount = new AtomicLong(0);
    private final AtomicLong fialureCount = new AtomicLong(0);

    // 有新的请求
    public void newRequestReceive() {
        requestCount.incrementAndGet();
    }

    // 处理成功
    public void requestProcessSuccess() {
        successCount.incrementAndGet();
    }

    // 处理失败
    public void requestProcessFialure() {
        fialureCount.incrementAndGet();
    }

    // 查看总数
    public long getRequestCount() {
        return requestCount.get();
    }

    // 获取成功总数
    public long getSuccessCount() {
        return successCount.get();
    }

    // 获取失败总数
    public long getFialureCount() {
        return fialureCount.get();
    }
}

/**
 * 模拟服务器的请求总数, 处理成功数,处理失败数
 */
public class Test01 {
    public static void main(String[] args) throws InterruptedException {
        // 通过线程模拟请求，在实际应用中可以在ServletFilter中调用Indicator计数器的相关方法
        for (int i = 0; i < 10000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 每个线程就是一个请求，请求总数要加1
                    Indicator.getInstance().newRequestReceive();
                    int num = new Random().nextInt();
                    // 偶数模拟成功
                    if (num % 2 == 0) {
                        Indicator.getInstance().requestProcessSuccess();
                    }
                    // 奇数模拟失败
                    else {
                        Indicator.getInstance().requestProcessFialure();
                    }
                }
            }).start();
        }
        Thread.sleep(1000);
        // 打印结果
        System.out.println("总请求数：" + Indicator.getInstance().getRequestCount());
        System.out.println("成功数：" + Indicator.getInstance().getSuccessCount());
        System.out.println("失败数：" + Indicator.getInstance().getFialureCount());
    }

}
