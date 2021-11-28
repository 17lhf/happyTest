package com.basic.happytest.modules.threadVariable;

public class VolatileTestThread implements Runnable{
    private boolean active;
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
