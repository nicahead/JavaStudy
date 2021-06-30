package me.nic.group;

/**
 * 创建线程组
 */
public class Test01 {
    public static void main(String[] args) {
        // 当前main线程的线程组
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        System.out.println("mainGroup: " + mainGroup); // mainGroup: java.lang.ThreadGroup[name=main,maxpri=10]

        // 定义线程组。如果不指定所属线程组，则自动归属到当前线程所属的线程组中
        ThreadGroup group1 = new ThreadGroup("group1");
        System.out.println("group1: " + group1); // group1: java.lang.ThreadGroup[name=group1,maxpri=10]

        // 定义线程组，同时指定父线程组
        ThreadGroup group2 = new ThreadGroup(mainGroup, "group2");
        System.out.println("group2: " + group2);  // group2: java.lang.ThreadGroup[name=group2,maxpri=10]

        // 现在 group1 与 group2 都是 maingroup 线程组中的子线程组, 调用线程组的 getParent()方法返回父线程组
        System.out.println(group1.getParent() == group2.getParent());  // true
        System.out.println(group1.getParent() == mainGroup);  // true

        // 创建线程时指定所属线程组
        // 在创建线程时,如果没有指定线程组,则默认线程归属到父线程的线程组中
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread());
            }
        };
        // 在main线程中创建了t1线程,称main线程为父线程,t1线程为子线程,
        // t1没有指定线程组则t1线程就归属到父线程main线程的线程组中
        Thread t1 = new Thread(runnable, "t1");
        System.out.println(t1);  // Thread[t1,5,main]

        // 创建线程时,可以指定线程所属线程组
        Thread t2 = new Thread(group1, runnable, "t1");
        Thread t3 = new Thread(group2, runnable, "t2");
        System.out.println(t2);  // Thread[t1,5,group1]
        System.out.println(t3);  // Thread[t2,5,group2]
    }
}
