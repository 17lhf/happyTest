package com.basic.happytest.modules.threadVariable;

/**
 * 多线程中的变量运行过程中的值变动问题使用线程之一
 * @author lhf
 */

public class VolatileTestThread1 implements Runnable{
    private volatile boolean active;
    private int anInt = 0;

    @Override
    public void run() {
        active = true;
        System.out.println("begin..." + anInt);
        while (active) {
            anInt ++;
        }
        System.out.println("end..." + anInt);
    }

    public void stop() {
        System.out.println("stop..." + anInt);
        active = false;
    }
}
