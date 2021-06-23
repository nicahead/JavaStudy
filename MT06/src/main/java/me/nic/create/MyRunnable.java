package me.nic.create;

/**
 * * 方法2：当线程类已经有父类了,就不能用继承 Thread 类的形式创建线程,可以使用实现 Runnable接口的形式
 */
// 1)定义类实现 Runnable 接口
public class MyRunnable implements Runnable {

    // 2)重写 Runnable 接口中的抽象方法 run(), run()方法就是子线程要执行的代码
    @Override
    public void run() {
        for (int i = 1; i <= 1000; i++) {
            System.out.println("sub thread --> " + i);
        }
    }

    public static void main(String[] args) {
        // 3) 创建 Runnable 接口的实现类
        MyRunnable myRunnable = new MyRunnable();
        // 4) 创建线程对象
        Thread thread = new Thread(myRunnable);
        // 5) 开启线程
        thread.start();
        for (int i = 1; i <= 1000; i++) {
            System.out.println("main --> " + i);
        }
    }

    /**
     * 有时也会使用匿名内部类的方式传递Runnable对象
     * @param args
     */
//    public static void main(String[] args) {
//
//        // 1) 创建线程对象,传入Runnable实现类 lambda表达式
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 1; i <= 1000; i++) {
//                    System.out.println("sub thread --> " + i);
//                }
//            }
//        });
//        // 2) 开启线程
//        thread.start();
//        for (int i = 1; i <= 1000; i++) {
//            System.out.println("main --> " + i);
//        }
//    }
}
