package com.liangzhicheng.six;

/**
 * 线程的状态：
 * 新建（NEW）：新创建一个线程对象
 * 可运行（RUNNABLE）：线程对象创建后，其他线程（比如main线程）调用了该对象的start()方法，该状态的线程位于可运行线程池中，等待被线程调度选中，获取cpu的使用权
 * 运行（RUNNING）：可运行状态（runnable）的线程获得cpu时间片（timeslice），执行程序代码
 * 阻塞（BLOCKED）：阻塞状态是指线程因为某种原因放弃cpu使用权，也即让出cpu timeslice，暂时停止运行，直到线程进入可运行（runnable）状态，才有机会再次获得cpu timeslice
 *                 转到运行（running）状态
 * 阻塞的三种情况：等待阻塞，运行（running）的线程执行o.wait()方法，JVM会把该线程放入等待队列（waitting queue）中
 *                同步阻塞，运行（running）的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入锁池（lock pool）中
 *                其他阻塞，运行（running）的线程执行Thread.sleep(long ms)或t.join()方法，或者发出I/O请求时，JVM会把该线程设置为阻塞状态，当sleep()状态超时、join()等待
 *                线程终止或者超时、或者I/O处理完毕时，线程重新转入可运行（runnable）状态
 * 死亡（DEAD）：线程run()、main()方法执行结束，或者因异常退出了run()方法，则该线程结束生命周期，死亡的线程不可再次复生
 *
 * 线程的通讯方法：
 * wait()方法使得当前线程必须要等待，等到另外一个线程调用notify()或者notifyAll()方法
 * notify()方法会唤醒一个等待当前对象的锁的线程，而notifyAll()是唤醒所有在等待中的线程
 * wait()和notify()方法要求在调用时线程已经获得了对象的锁，因此对这两个方法的调用需要放在synchronized方法或synchronized代码块中
 */
public class ProviderAndConsumer1 {
    //生产者消费者案例熟悉线程通讯的方法
    //1.传统线程通信synchronized + wait + notify
    public static void main(String[] args) {
        Execute execute = new Execute();
        new Thread(new Provider(execute), "生产者A").start();
        new Thread(new Provider(execute), "生产者B").start();
        new Thread(new Consumer(execute), "消费者A").start();
        new Thread(new Consumer(execute), "消费者B").start();
    }

}

class Provider implements Runnable {

    Execute execute;

    public Provider (Execute execute) {
        this.execute = execute;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            execute.provider();
        }
    }

}

class Consumer implements Runnable {

    Execute execute;

    public Consumer (Execute execute) {
        this.execute = execute;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            execute.consumer();
        }
    }

}

class Execute {

    private int productNum = 0;

    public synchronized void provider() {
        while (productNum >= 1) {
            System.out.println("库存已满");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "数量为:" + ++productNum);
        notifyAll();
    }

    public synchronized void consumer() {
        while (productNum <= 0) {
            System.out.println("目前缺货");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "数量为:" + --productNum);
        notifyAll();
    }

}
