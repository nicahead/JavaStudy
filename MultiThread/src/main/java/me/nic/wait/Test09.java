package me.nic.wait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 在多线程环境中，把字符串转换为日期对象
 * 多个线程使用同一个 SimpleDateFormat 对象可能会产生线程安全问题，有异常
 * 为每个线程指定自己的 SimpleDateFormat 对象，使用 ThreadLocal
 */
public class Test09 {

    static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

    static class ParseDate implements Runnable {
        private int i = 0;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                // 构建日期字符串，秒的值不同
                String text = "2068 年 11 月 22 日 08:28:" + i % 60;
                // 先判断当前线程是否有SimpleDateFormat对象，如果没有需要创建一个
                if (threadLocal.get() == null) {
                    threadLocal.set(new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss"));
                }
                // 使用当前线程的SimpleDateFormat对象进行解析字符串
                Date date = threadLocal.get().parse(text);
                System.out.println(i + "--" + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        // 创建100个线程
        for (int i = 0; i < 100; i++) {
            new Thread(new ParseDate(i)).start();
        }
    }
}
