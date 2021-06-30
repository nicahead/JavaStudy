package me.nic.wait;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * me.nic.wait 条件发生变化
 * 定义一个集合
 * 定义一个线程向集合中添加数据，添加完数据后通知另外的线程从集合中取数据
 * 定义一个线程从集合中取数据，如果集合中没有数据就等待
 */
public class Test07 {

    static final List<Integer> list = new ArrayList<>();
    static Random random = new Random();

    // 添加元素到链表尾部
    static class AddThread extends Thread {
        @Override
        public void run() {
            synchronized (list) {
                int item = random.nextInt();
                System.out.println("添加一个元素：" + item);
                list.add(item);
                list.notify();
            }
        }
    }

    // 取出链表尾部
    static class GetThread extends Thread {
        @Override
        public void run() {
            synchronized (list) {
                // 注意，这里的条件判断应该用while
                // 如果用if，当前线程被唤醒并竞争到锁之后，直接执行下面的取数据逻辑，万一list为空，就会有空指针异常
                // 用while的话竞争到锁之后还会再去判断一下list是否为空（条件发生了变化），为空还会再去wait
                if (list.size() == 0) {
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 取出这个数据
                System.out.println("取出一个元素：" + list.get(0));
            }
        }
    }

    public static void main(String[] args) {
        AddThread add = new AddThread();
        GetThread get = new GetThread();
        // 测试一: 先开启添加数据的线程,再开启一个取数据的线程,大多数情况下会正常
        // add.start();
        // get.start();

        // 测试二: 先开启取数据的线程,再开启添加数据的线程, 取数据的线程会先等待, 等到添加数据之后, 再取数据
        // get.start();
        // add.start();

        // 测试三: 开启两个取数据的线程, 再开启添加数据的线程
        GetThread get2 = new GetThread();
        get.start();
        get2.start();
        add.start();
    }
}
