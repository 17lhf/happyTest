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
