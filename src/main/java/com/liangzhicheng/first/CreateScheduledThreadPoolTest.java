package com.liangzhicheng.first;

import java.util.concurrent.*;

/**
 * 创建固定大小线程池，可以延迟或定时的执行任务
 */
public class CreateScheduledThreadPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //方式4线程池
        //1.创建固定大小线程池
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 5; i++) {
            //可传入Runnable
//            scheduledThreadPool.schedule(new ThreadPoolDemo2(), 1L, TimeUnit.SECONDS);
            //可传入Callable
            ScheduledFuture<Integer> result = scheduledThreadPool.schedule(new ScheduledThreadPoolCallableDemo(), 1L, TimeUnit.SECONDS);
            System.out.println(result.get());
        }
        scheduledThreadPool.shutdown();
    }

}

class ThreadPoolDemo2 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }

}

class ScheduledThreadPoolCallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += i;
        }
        return sum;
    }

}
