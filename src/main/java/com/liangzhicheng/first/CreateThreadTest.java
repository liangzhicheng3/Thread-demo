package com.liangzhicheng.first;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CreateThreadTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //方式1实现Runnable接口
//        RunnableDemo runnableDemo = new RunnableDemo();
//        new Thread(runnableDemo).start();

        //方式2继承Thread类
//        new ThreadDemo().start();

        //方式3实现Callable接口
        CallableDemo callableDemo = new CallableDemo();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(callableDemo);
        new Thread(futureTask).start();
        //获取CallableDemo类中call方法返回值
        Integer result = futureTask.get();
        System.out.println(result);

    }

}

//方式1实现Runnable接口
class RunnableDemo implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }

}

//方式2继承Thread类
class ThreadDemo extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }

}

//方式3实现Callable接口
class CallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += i;
        }
        return sum;
    }

}
