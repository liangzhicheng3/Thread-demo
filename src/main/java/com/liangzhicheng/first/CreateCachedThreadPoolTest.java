package com.liangzhicheng.first;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 概念：
 * 用到线程池主要是避免并发中每一次任务都创建线程来处理，频繁的创建线程和销毁线程也是需要时间，降低了性能
 * 线程池思想和数据库连接池思想基本上一致
 *
 * 体系结构：
 * java.util.concurrent.Executor：负责线程的使用与调度的根接口
 *   |--ExecutorService：子接口，线程池的主要接口
 *     |--ThreadPoolExecutor：线程池的实现类
 *     |--ScheduledExecutorService：子接口，负责线程的调度
 *       |--ScheduledThreadPoolExecutor：继承ThreadPoolExecutor，实现ScheduledExecutorService
 *
 * 创建缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程，线程池为无限大，
 * 当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程
 */
public class CreateCachedThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        //方式4线程池
        //1.创建缓存线程池
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            cachedThreadPool.execute(new ThreadPoolDemo3());
        }
        cachedThreadPool.shutdown();
    }

}

class ThreadPoolDemo3 implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }

}
