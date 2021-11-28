package com.basic.happytest.multithreadSync;

import com.basic.happytest.modules.multithreadSync.entity.MultithreadSync;
import com.basic.happytest.modules.multithreadSync.service.MultithreadSyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class MultithreadSyncTest {

    @Autowired
    MultithreadSyncService multithreadSyncService;


    /**
     * 如果2个线程同时调用方法，其中一个执行完后，由代理类负责事务的提交，在代理方法执行后、提交事务前这段空隙中，另外一个线程就会进入方法,
     * 开始查询，也就拿到了旧的值。
     * 打断点spring中TransactionAspectSupport的
     * commitTransactionAfterReturning方法，发现另外一个进程已经进入方法中，导致同步锁没有做到同步的效果。
     *
     * Transactional的范围比synchronized大
     */


    @Test
    public void testAdd() {
        // id = 1
        multithreadSyncService.add(0, "Transactional + synchronized + sql");
        // id = 2
        multithreadSyncService.add(0, "synchronized + sql");
        // id = 3
        multithreadSyncService.add(0, "Transactional + synchronized + mybatisPlus");
        // id = 4
        multithreadSyncService.add(0, "Transactional+ synchronized + no select + sql");
    }

    /**
     * 多线程并发测试:Transactional + synchronized + sql
     */
    @Test
    public void testMutiThreadIncrease1() throws InterruptedException {
        int threadNum = 600;
        Integer id = 1;
        // 先重置值
        MultithreadSync multithreadSync = multithreadSyncService.selectObjectById(id);
        multithreadSync.setValueGen(0);
        multithreadSyncService.updateValueById(multithreadSync);
        // 然后再开始并发测试
        CountDownLatch cdl = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                cdl.countDown();
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                multithreadSyncService.increaseTS(id);
            }).start();
        }
        Thread.sleep(120000);
        // value_gen结果： 300
    }

    /**
     * 多线程并发测试:synchronized + sql
     */
    @Test
    public void testMutiThreadIncrease2() throws InterruptedException {
        int threadNum = 600;
        Integer id = 2;
        // 先重置值
        MultithreadSync multithreadSync = multithreadSyncService.selectObjectById(id);
        multithreadSync.setValueGen(0);
        multithreadSyncService.updateValueById(multithreadSync);
        // 然后再开始并发测试
        CountDownLatch cdl = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                cdl.countDown();
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                multithreadSyncService.increaseS(id);
            }).start();
        }
        Thread.sleep(120000);
        // value_gen结果： 600
    }

    /**
     * 多线程并发测试：Transactional + synchronized + mybatisPlus
     */
    @Test
    public void testMutiThreadIncrease3() throws InterruptedException {
        int threadNum = 600;
        Integer id = 3;
        // 先重置值
        MultithreadSync multithreadSync = multithreadSyncService.selectObjectById(id);
        multithreadSync.setValueGen(0);
        multithreadSyncService.updateValueById(multithreadSync);
        // 然后再开始并发测试
        CountDownLatch cdl = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                cdl.countDown();
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                multithreadSyncService.increaseMB(id);
            }).start();
        }
        Thread.sleep(120000);
        // value_gen结果： 301
    }

    /**
     * 多线程并发测试：Transactional+ synchronized + no select + sql
     */
    @Test
    public void testMutiThreadIncrease4() throws InterruptedException {
        int threadNum = 600;
        Integer id = 4;
        // 先重置值
        MultithreadSync multithreadSync = multithreadSyncService.selectObjectById(id);
        multithreadSync.setValueGen(0);
        multithreadSyncService.updateValueById(multithreadSync);
        // 然后再开始并发测试
        CountDownLatch cdl = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                cdl.countDown();
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                multithreadSyncService.increaseNoSelect(id);
            }).start();
        }
        Thread.sleep(120000);
        // value_gen结果： 600
    }

}
