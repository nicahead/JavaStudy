package me.nic.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicIntegerFieldUpdater示例
 */
// 实体类
class User {
    int id;
    // 使用 AtomicIntegerFieldUpdater 更新的字段必须使用 volatile 修饰
    volatile int age;

    public User(int id, int age) {
        this.id = id;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", age=" + age + '}';
    }
}

// 线程类
class SubThread extends Thread {

    // 要更新的User对象
    private User user;

    // 创建AtomicIntegerFieldUpdater更新器
    private AtomicIntegerFieldUpdater<User> updater;

    public SubThread(User user) {
        this.user = user;
        updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
    }

    @Override
    public void run() {
        // 在子线程中对user对象的age字段自增10次
        for (int i = 0; i < 10; i++) {
            updater.getAndIncrement(user);
        }
    }
}

public class Test06 {
    public static void main(String[] args) throws InterruptedException {
        User user = new User(1234, 10);
        // 开启10个线程
        for (int i = 0; i < 10; i++) {
            new SubThread(user).start();
        }
        Thread.sleep(1000);
        System.out.println(user);
    }
}

