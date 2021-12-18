package com.liangzhicheng.ten;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch（闭锁）：
 * 一个同步工具类，它允许一个或多个线程一直等待，直到其他线程的操作执行完后再执行
 *
 * 应用场景：CountDownLatch类位于java.util.concurrent包下，利用它可以实现类似计数器的功能，比如有一个任务A，
 * 它要等待其他几个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        /**
         * 指定50数量，每次试行后countDownLatch就会减1，直到减到0，此时就会执行countDownLatch.await()往下代码
         */
        CountDownLatch countDownLatch = new CountDownLatch(50);
        CountDownLatchDemo countDownLatchDemo = new CountDownLatchDemo(countDownLatch);
        Instant start = Instant.now();
        for (int i = 0; i < 50; i++) {
            new Thread(countDownLatchDemo).start();
        }
        try {
            countDownLatch.await(); //这里需要等待，执行完上面操作才能往下执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();
        System.out.println("消耗时间:" + Duration.between(start, end));
    }

}

class CountDownLatchDemo implements Runnable{

    private CountDownLatch countDownLatch;

    public CountDownLatchDemo(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        try{
            int sum = 0;
            for (int i = 0; i < 50000; i++) {
                if(i % 2 == 0){
                    sum += i;
                }
            }
            System.out.println(Thread.currentThread().getName() + ":" + sum);
        }finally{
            countDownLatch.countDown();
        }
    }

}
