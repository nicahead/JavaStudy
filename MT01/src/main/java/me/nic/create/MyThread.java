package me.nic.create;

/**
 * 方法1：继承 Thread类
 */
// 1)定义类继承 Thread
public class MyThread extends Thread {
    // 2) 重写 Thread 父类中的 run()
    @Override
    public void run() {
        System.out.println("我是子线程");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("启动主线程");
        // 3) 创建子线程对象
        MyThread myThread1 = new MyThread();
        myThread1.start();
        /*
            调用线程的 start()方法来启动线程, 启动线程的实质就是请求 JVM 运行相应的
            线程,这个线程具体在什么时候运行由线程调度器(Scheduler)决定
            注意:
                start()方法调用结束并不意味着子线程开始运行
                新开启的线程会执行 run()方法
                如果开启了多个线程,start()调用的顺序并不一定就是线程启动的顺序
                多线程运行结果与代码执行顺序或调用顺序无关
        */
        System.out.println("主线程其他内容");
    }
}
