package com.basic.happytest.modules.threadVariable;

public class VolatileTestThread2 implements Runnable{
    private boolean active;
    private int anInt = 0;

    @Override
    public void run() {
        active = true;
        System.out.println("begin..." + anInt);
        while (active) {
            System.out.println("run..." + anInt);
//            synchronized (this) {
//                for (int i = 0; i < 100; i++) {
//                }
//            }
            anInt ++;
        }
        System.out.println("end..." + anInt);
    }

    public void stop() {
        System.out.println("stop..." + anInt);
        active = false;
        // System.out.println("stop..." + anInt);
    }
}