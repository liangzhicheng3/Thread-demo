package com.liangzhicheng.second;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 概念：
 * 必要的情况下，将一个大任务，进行拆分（fork）成若干个小任务（拆到不可再拆），再将一个个小任务运算的结果进行汇总
 *
 * ForkJoin与线程池的区别：
 * 窃取模式，当执行新的任务时它可以将其拆分成更小的任务执行，并将小任务加到线程队列中，再从一个随机线程的队列中偷一个并把它放到自己的队列中
 * 相对于一般的线程池实现，ForkJoin的优势体现在对其中包含的任务的处理方式上，在一般的线程池中，如果一个线程正在执行的任务由于某些原因无法继续运行，
 * 那么该线程会处于等待状态，而在ForkJoin实现中，如果某个子问题由于等待另一个子问题的完成而无法继续运行，那么处理该子问题的线程会主动寻找其他尚未
 * 运行的子问题来执行，这种方式减少了线程的等待时间，提高性能
 */
public class ForkJoinTest {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Instant start = Instant.now();
        long result = forkJoinPool.invoke(new ForkJoinDemo(0, 10000000000L)); //用ForkJoin框架求和一百亿耗时
        Instant end = Instant.now();
        System.out.println("消耗时间:" + Duration.between(start, end)); //1222ms
        System.out.println("求和结果:" + result); //-5340887581128765873

        //单线程中求和一百亿耗时
//        long start = System.currentTimeMillis();
//        long sum = 0L;
//        for (long i = 0; i < 10000000000L; i++) {
//            sum += i;
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("消耗时间:" + (end - start)); //4938ms
//        System.out.println("求和结果:" + sum); //-5340232226128654848
    }

}

class ForkJoinDemo extends RecursiveTask<Long> {

    private long start;
    private long end;

    //临界点，拆分到什么位置就不能拆分
    public static final long BORDER = 100000L;

    public ForkJoinDemo(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;
        if(length <= BORDER){
            long sum = 0L;
            for (long i = start; i < end; i++) {
                sum += i;
            }
            return sum;
        }else{
            //大于临界值继续拆分
            long middle = (start + end) / 2;
            ForkJoinDemo left = new ForkJoinDemo(start, middle);
            left.fork(); //继续调用compute方法拆分
            ForkJoinDemo right = new ForkJoinDemo(middle + 1, end);
            right.fork(); //继续调用compute方法拆分
            //拆分完后返回合并的值
            return left.join() + right.join();
        }
    }

}
