package me.nic.atomic;

import java.util.*;

class Solution {

    // 结果，不能有重复，使用set
    Set<String> res = new HashSet<>();
    // 做选择时，使用index存储已使用的字符的下标，使用set是防止list删除元素的时候按照下标删除
    Set<Integer> index = new HashSet<>();
    // 记录一个结果
    StringBuffer path = new StringBuffer();
    // 所有可以选择的字母
    char[] choices;

    public String[] permutation(String s) {
        choices = s.toCharArray();
        backTrace();
        return res.toArray(new String[res.size()]);
    }

    private void backTrace() {
        // 排列完成
        if (path.length() == choices.length) {
            res.add(new String(path));
            return;
        }
        for (int i = 0; i < choices.length; i++) {
            // 当前字符已经使用过，剪枝
            if (index.contains(i)) continue;
            // 做选择，添加到方案中
            path.append(choices[i]);
            index.add(i);
            // 回溯
            backTrace();
            // 撤销选择
            path.deleteCharAt(path.length() - 1);
            index.remove(i);
        }
    }
}


//
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicMarkableReference;
//
///**
// * AtomicMarkableReference是将一个boolean值作是否有更改的标记，本质就是它的版本号只有两个，true和false，
// * 修改的时候在这两个版本号之间来回切换，这样做并不能解决ABA的问题，只是会降低ABA问题发生的几率而已
// */
//public class Test {
//    private static AtomicMarkableReference atomicMarkableReference = new AtomicMarkableReference(100, false);
//
//    public static void main(String[] args) {
//        Thread refT1 = new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            atomicMarkableReference.compareAndSet(100, 101, atomicMarkableReference.isMarked(), !atomicMarkableReference.isMarked());
//            atomicMarkableReference.compareAndSet(101, 100, atomicMarkableReference.isMarked(), !atomicMarkableReference.isMarked());
//        });
//        Thread refT2 = new Thread(() -> {
//            boolean marked = atomicMarkableReference.isMarked();
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            boolean c3 = atomicMarkableReference.compareAndSet(100, 101, marked, !marked);
//            System.out.println(c3); // 返回true,实际应该返回false
//        });
//        refT1.start();
//        refT2.start();
//    }
//}
