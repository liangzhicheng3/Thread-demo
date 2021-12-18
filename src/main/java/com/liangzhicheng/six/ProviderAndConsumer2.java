package com.liangzhicheng.six;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProviderAndConsumer2 {
    //生产者消费者案例熟悉线程通讯的方法
    //2.使用Condition控制线程通信lock + condition + await + signal
    public static void main(String[] args) {
        Execute1 execute1 = new Execute1();
        new Thread(new Provider1(execute1), "生产者A").start();
        new Thread(new Provider1(execute1), "生产者B").start();
        new Thread(new Consumer1(execute1), "消费者A").start();
        new Thread(new Consumer1(execute1), "消费者B").start();
    }

}

class Provider1 implements Runnable {

    Execute1 execute1;

    public Provider1(Execute1 execute1) {
        this.execute1 = execute1;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            execute1.provider();
        }
    }

}

class Consumer1 implements Runnable {

    Execute1 execute1;

    public Consumer1(Execute1 execute1) {
        this.execute1 = execute1;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            execute1.consumer();
        }
    }

}

class Execute1 {

    private int productNum = 0;

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void provider() {
        lock.lock();
        try {
            while (productNum >= 1) {
                System.out.println("库存已满");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "数量为:" + ++productNum);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void consumer() {
        lock.lock();
        try {
            while (productNum <= 0) {
                System.out.println("目前缺货");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "数量为:" + --productNum);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

}
