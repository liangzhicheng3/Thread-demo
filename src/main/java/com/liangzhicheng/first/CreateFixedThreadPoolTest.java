package com.liangzhicheng.first;

import java.util.concurrent.*;

/**
 * 创建定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
 */
public class CreateFixedThreadPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //方式4线程池
        //1.创建定长线程池
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        //2.将线程交给定长线程池管理
        for (int i = 0; i < 5; i++) {
            //可传入Runnable
//            fixedThreadPool.submit(new ThreadPoolDemo1());
            //可传入Callable
            Future<Integer> result = fixedThreadPool.submit(new FixedThreadPoolCallableDemo());
            System.out.println(result.get());
        }
        //3.关闭(实际上是放回连接池中)
        fixedThreadPool.shutdown();
    }

}

class ThreadPoolDemo1 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }

}

class FixedThreadPoolCallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += i;
        }
        return sum;
    }

}
