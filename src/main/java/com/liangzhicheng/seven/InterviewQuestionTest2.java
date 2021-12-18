package com.liangzhicheng.seven;

/**
 * 现有T1、T2、T3三个线程，怎样保证T2在T1执行完后执行，T3在T2执行完后执行
 * 线程中有一个join()方法，表示调用join()方法的线程要先执行完才会执行其他线程
 */
public class InterviewQuestionTest2 {

    public static void main(String[] args) throws InterruptedException {
//        method1();
        method2();
    }

    //方式1
    private static void method1() throws InterruptedException {
        Thread t1 = new Thread(new T1());
        Thread t2 = new Thread(new T2());
        Thread t3 = new Thread(new T3());
        t1.start();
        t1.join();
        t2.start();
        t2.join();
        t3.start();
    }

    //方式2
    private static void method2() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("T1");
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T2");
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T3");
            }
        });
        t3.start();
        t2.start();
        t1.start();
    }

}

class T1 implements Runnable{

    @Override
    public void run() {
        System.out.println("T1");
    }

}

class T2 implements Runnable{

    @Override
    public void run() {
        System.out.println("T2");
    }

}

class T3 implements Runnable{

    @Override
    public void run() {
        System.out.println("T3");
    }

}
