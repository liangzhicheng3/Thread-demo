package com.liangzhicheng.fourth;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    public static void main(String[] args) {
        //10个线程同时对num进行自增，会出现相同的数值
        //要解决上述问题用到java.util.concurrent.atomic下的包方法（解决多线程并发的问题），底层用CAS算法
        AtomicDemo atomicDemo = new AtomicDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(atomicDemo).start();
        }
    }

}

//class AtomicDemo implements Runnable {
//
//    private int num = 0;
//
//    @Override
//    public void run() {
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(getNumAdd());
//    }
//
//    private int getNumAdd() {
//        return num++;
//    }
//
//}

class AtomicDemo implements Runnable {

    private AtomicInteger num = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getNumAdd());
    }

    private int getNumAdd() {
        return num.getAndIncrement();
    }

}
