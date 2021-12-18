package com.liangzhicheng.eight;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 使用CopyOnWriteArrayList来解决并发边迭代边往集合中添加元素
 */
public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        CopyOnWriteArrayListDemo copyOnWriteArrayListDemo = new CopyOnWriteArrayListDemo();
        for (int i = 0; i < 2; i++) {
            new Thread(copyOnWriteArrayListDemo).start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class CopyOnWriteArrayListDemo implements Runnable{

    private static final CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList<>();

    static {
        arrayList.add("AA");
        arrayList.add("BB");
        arrayList.add("CC");
    }

    @Override
    public void run() {
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
            arrayList.add("DD");
        }
        System.out.println(arrayList);
    }

}
