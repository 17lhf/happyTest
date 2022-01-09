package com.basic.happytest.modules.threadVariable;

import com.basic.happytest.modules.threadVariable.VolatileTestThread;
import com.basic.happytest.modules.threadVariable.VolatileTestThread1;
import com.basic.happytest.modules.threadVariable.VolatileTestThread2;
import com.basic.happytest.modules.threadVariable.VolatileTestThread3;
import org.junit.jupiter.api.Test;

/**
 * 多线程中的变量运行过程中的值变动问题
 * @author lhf
 * 参考阅读：深入理解Thread.sleep()的意义： https://blog.csdn.net/agony_sun/article/details/78031520
 * 文章地址：https://www.bilibili.com/read/cv14195465
 */

public class ThreadVariableTest {

    /**
     * 寻常情况下，线程执行过程中突然对其变量进行变动
     * @throws InterruptedException 异常
     */
    @Test
    public void testNormal() throws InterruptedException {
        VolatileTestThread volatileTestThread = new VolatileTestThread();
        Thread thread = new Thread(volatileTestThread);
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        volatileTestThread.stop();
        Thread.sleep(2000);
        // 结果：while死循环，while一直在用旧的active，符合预期
    }

    /**
     * 对变量使用volatile关键字修饰情况下，线程执行过程中突然对其变量进行变动
     * @throws InterruptedException 异常
     */
    @Test
    public void testVolatile() throws InterruptedException {
        VolatileTestThread1 volatileTestThread1 = new VolatileTestThread1();
        Thread thread = new Thread(volatileTestThread1);
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        volatileTestThread1.stop();
        Thread.sleep(2000);
        // 结果：stop后结束循环，while拿到的新的active，符合预期
    }

    /**
     * 在while循环中加入输入打印语句下，线程执行过程中突然对其变量进行变动
     * @throws InterruptedException 异常
     */
    @Test
    public void testOutput() throws InterruptedException {
        VolatileTestThread2 volatileTestThread2 = new VolatileTestThread2();
        Thread thread = new Thread(volatileTestThread2);
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        volatileTestThread2.stop();
        Thread.sleep(2000);
        // 结果：stop后结束循环，while拿到的新的active 因为
    }

    /**
     * 在while循环中加入Sleep下，线程执行过程中突然对其变量进行变动
     * @throws InterruptedException 异常
     */
    @Test
    public void testSleep() throws InterruptedException {
        VolatileTestThread3 volatileTestThread3 = new VolatileTestThread3();
        Thread thread = new Thread(volatileTestThread3);
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        volatileTestThread3.stop();
        Thread.sleep(2000);
        // 结果：stop后结束循环，while拿到的新的active 因为
    }
}
