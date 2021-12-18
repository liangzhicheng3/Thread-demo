package com.liangzhicheng.nine;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：
 * 多个读操作可以同时进行
 * 写操作必须互斥（只允许一个写操作，也不能读操作写操作同时进行）
 * 写操作优先于读操作（一旦有写操作，后续读操作必须等待，唤醒时优先考虑写操作）
 *
 * 在读操作远远大于写操作时，可使用读写锁操作，一般情况下独占锁的效率低来源于高并发下对临界区的激烈竞争导致线程上下文切换
 * 而读写锁由于需要额外维护读锁的状态，有时候可能还不如独占锁的效率高，因此需要根据场景选择使用
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        //写操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                readWriteLockDemo.set((int) Math.random() * 101);
            }
        }, "写操作").start();
        //读操作
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readWriteLockDemo.get();
                }
            }).start();
        }
    }

}

class ReadWriteLockDemo{

    private int num;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    //读操作
    public void get(){
        lock.readLock().lock();
        try{
            System.out.println("read " + Thread.currentThread().getName());
        } finally {
            lock.readLock().unlock();
        }
    }

    //写操作
    public void set(int num){
        lock.writeLock().lock();
        try{
            System.out.println("write "+ Thread.currentThread().getName());
            this.num = num;
        }finally{
            lock.writeLock().unlock();
        }
    }

}
