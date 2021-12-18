package com.liangzhicheng.third;

/**
 * JVM内存模型概念：内存模型规定了所有的变量都存储在主内存中，每条线程还有自己的工作内存，线程的工作内存保存了该线程使用到的变量的主内存的副本拷贝，
 * 线程对变量的所有操作都必须在工作内存中操作，而不能直接对主内存进行读写操作，不同的线程之间也不能相互访问，线程之间的传递值都需要通过主内存来完成
 *
 * 内存之间交互：
 * read（读取）：作用于主内存变量，把一个变量值从主内存传输到线程的工作内存中，以便随后的load动作使用
 * load（载入）：作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量副本中
 * use（使用）：作用于工作内存的变量，把工作内存中的一个变量值传递给执行引擎，每当虚拟机遇到一个需要使用变量的值的字节码指令时将会执行这个操作
 * assign（赋值）：作用于工作内存的变量，它把一个从执行引擎接收到的值赋值给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作
 * store（存储）：作用于工作内存的变量，把工作内存中的一个变量的值传送到主内存中，以便随后的write的操作
 * write（写入）：作用于主内存的变量，它把store操作从工作内存中一个变量的值传送到主内存的变量中
 *
 * JVM对交互命令的约束：
 * 不允许read和local、store和write操作之一单独出现
 * 不允许一个线程丢弃它的最近assign操作，即变量在工作内存中改变之后必须同步到主内存中
 * 不允许一个线程无原因（没有发生过任何assign操作）把数据从工作内存同步回主内存中
 * 一个新的变量只能在主内存中诞生，不允许在工作内存中直接使用一个未被初始化（load或assign）的变量，即就是对一个变量实施use和store操作之前，必须先执行过assign和load
 *
 * 打印结果是Thread-0:true
 * 为什么没有打印//////
 * 因为while(true)调用jvm底层一些方法执行过快，且自定义线程也睡了2s，从而无法从线程中修改后的值在主内存中获取，所以一直获取的值是最开始的false
 * 要解决这个问题那在定义变量时候加修饰符volatile
 * volatile:
 *    1.使用volatile关键字会强制将修改的值立即写入主存
 *    2.使用volatile关键字的话，当线程2进行修改时，会导致线程1的工作内存中缓存变量store的缓存行无效
 *    3.由于线程1的工作内存中缓存变量stop的缓存行无效，所以线程1再次读取变量store的值时会去主存读取
 *
 * 当一个变量被volatile修饰后，具备两个特性
 * 可见性：
 * 保证次变量对所有线程可见，指当一个线程修改这个变量的值，这个修改的值对其他线程是立即得知，而普通的变量的值传递需要通过主内存完成
 * 例如线程A修改一个变量的值，向主内存写入，线程B在线程A写入完成后在从主内存进行读写操作，新变量的值才会被线程B读到
 * 禁止指令重新排序优化：
 * 不同的变量仅仅会保证在该方法的执行过程中所有赋值结果的地方都能获取到正确的结果，而不能保证变量赋值的操作顺序，
 * 因为JVM默认会对代码的执行顺序做出优化，虽然结果一样但是顺序不一致，而volatile会禁止重排序
 * 在进行指令优化时，不能将在对volatile变量访问的语句放在起后面执行，也不能把volatile变量后面的语句放到其前面执行
 *
 * volatile的原理和实现机制：
 * 在使用volatile关键字所生成的汇编代码中会多出一个lock前缀指令
 * lock前缀指令实际上相当于一个内存屏障，内存屏障会提供3个功能：
 * 它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面，即在执行到内存屏障这句指令时，在它前面的操作已经全部完成
 * 它会强制将对缓存的修改操作立即写入主内存
 * 如果是写操作，它会导致其他cpu中对应的缓存行无效
 *
 * volatile与synchronized区别：
 * 更加轻量级，volatile不会像synchronized会出现阻塞状况，volatile无法保证原子性，而synchronized是三个特性都满足
 */
public class JVMModelTest {

    public static void main(String[] args) {
        JVMModelDemo jvmModelDemo = new JVMModelDemo();
        new Thread(jvmModelDemo).start();
//        while(true){
//            if(jvmModelDemo.isFlag()){
//                System.out.println("//////");
//                break;
//            }
//        }
        for (int i = 0; i < 10000; i++) {
            System.out.println(1);
            if(jvmModelDemo.isFlag()){
                System.out.println(2);
                System.out.println("//////");
                break;
            }
        }
    }

}

class JVMModelDemo implements Runnable {

    private volatile boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        System.out.println(3);
        flag = true;
        System.out.println(4);
        System.out.println(Thread.currentThread().getName() + ":" + flag);
    }

}
