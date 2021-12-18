package com.liangzhicheng.five;

import java.util.Random;

/**
 * CAS算法是硬件对于并发操作的支持，包含了三个值，内存值V、预估值A、更新值B，当且仅当V == A时，V = B，否则不会执行任何操作
 */
public class CASTest {

    public static void main(String[] args) {
        CASDemo casDemo = new CASDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int expectValue = casDemo.get();
                    System.out.println(expectValue);
                    boolean flag = casDemo.compareAndSet(expectValue, new Random().nextInt(100));
                    System.out.println(flag);
                }
            }).start();
        }
    }

}

class CASDemo{

    private int value;

    public int get(){
        return this.value;
    }

    public synchronized int compareAndSwap(int expectValue, int newValue){
        int oldValue = this.value;
        if(expectValue == oldValue){
            this.value = newValue;
        }
        return oldValue;
    }

    public boolean compareAndSet(int expectValue, int newValue){
        return expectValue == this.compareAndSwap(expectValue, newValue);
    }

}
