package com.liangzhicheng.seven;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 编写一个程序，开启3个线程，这三个线程的ID分别为 A、B、C，每个线程将自己的 ID 在屏幕上打印5遍，要求输出的结果必须按顺序显示
 * 如：ABCABCABC... 依次递归
 */
public class InterviewQuestionTest1 {

    public static void main(String[] args) {
        ABC abc = new ABC();
        new Thread(new ThreadA(abc), "A").start();
        new Thread(new ThreadB(abc), "B").start();;
        new Thread(new ThreadC(abc), "C").start();;
    }

}

class ThreadA implements Runnable{

    ABC abc;

    public ThreadA(ABC abc){
        this.abc = abc;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            abc.A();
        }
    }

}

class ThreadB implements Runnable{

    ABC abc;

    public ThreadB(ABC abc){
        this.abc = abc;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            abc.B();
        }
    }

}

class ThreadC implements Runnable{

    ABC abc;

    public ThreadC(ABC abc){
        this.abc = abc;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            abc.C();
        }
    }

}

class ABC{

    private int num = 1;

    Lock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();

    public void A(){
        lock.lock();
        try{
            if(num != 1){
                try{
                    condition1.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName());
            num = 2;
            condition2.signalAll();
        }finally{
            lock.unlock();
        }
    }

    public void B(){
        lock.lock();
        try{
            if(num != 2){
                try{
                    condition2.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName());
            num = 3;
            condition3.signalAll();
        }finally{
            lock.unlock();
        }
    }

    public void C(){
        lock.lock();
        try{
            if(num != 3){
                try {
                    condition3.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName());
            num = 1;
            condition1.signalAll();
        }finally{
            lock.unlock();
        }
    }

}
