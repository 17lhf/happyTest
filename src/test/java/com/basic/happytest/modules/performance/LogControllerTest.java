package com.basic.happytest.modules.performance;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : lhf
 */
class LogControllerTest {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LogControllerTest.class);
    private static final int LOOP_COUNT = 10000;

    @Test
    void logSystemOut() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            System.out.println("hello world");
        }
        long endTime = System.currentTimeMillis();
        logger.info("Logged messages in " + (endTime - startTime) + " ms");
    }

    @Test
    void printProperty() {
        String propertyOut = System.getProperty("sun.stdout.encoding");
        System.out.println("default out encoding: " + propertyOut); // default: null
        String propertyErr = System.getProperty("sun.stderr.encoding");
        System.out.println("default err encoding: " + propertyErr); // default: null
    }


    @Test
    void testSystemOut2File() throws FileNotFoundException {
        FileOutputStream fosOut = new FileOutputStream("stdout_output.txt");
        BufferedOutputStream bosOut = new BufferedOutputStream(fosOut, 128);
        PrintStream psOut = new PrintStream(bosOut, true);
        System.setOut(psOut);
        runPerformanceTest(true);
    }

    @Test
    void testSystemErr2File() throws FileNotFoundException {
        FileOutputStream fosErr = new FileOutputStream("stderr_output.txt");
        BufferedOutputStream bosErr = new BufferedOutputStream(fosErr, 128);
        PrintStream psErr = new PrintStream(bosErr, true);
        System.setErr(psErr);
        runPerformanceTest(false);
    }


    private void runPerformanceTest(boolean useSystemOut) {
        int numThreads = 1000;  // 线程数量
        int numWrites = 1000;  // 每个线程的写入次数
        int messageSize = 500;  // 每次写入的消息大小（字节）
        Thread[] threads = new Thread[numThreads];
        // 创建并启动线程
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new WriterTask(numWrites, messageSize, useSystemOut));
            threads[i].start();
        }
        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Performance test completed.");
    }

    static class WriterTask implements Runnable {
        private final int numWrites;
        private final int messageSize;
        private final boolean useSystemOut;

        public WriterTask(int numWrites, int messageSize, boolean useSystemOut) {
            this.numWrites = numWrites;
            this.messageSize = messageSize;
            this.useSystemOut = useSystemOut;
        }

        @Override
        public void run() {
            StringBuilder message = new StringBuilder(messageSize);
            for (int i = 0; i < messageSize; i++) {
                message.append('a');
            }

            long startTime = System.nanoTime();
            for (int i = 0; i < numWrites; i++) {
                if (useSystemOut) {
                    System.out.print(message);
                } else {
                    System.err.print(message);
                }
            }
            long endTime = System.nanoTime();

            long duration = endTime - startTime;
            double throughput = (numWrites * messageSize) / (duration / 1_000_000_000.0);  // 吞吐量 (MB/s)

            if (useSystemOut) {
                System.out.printf("System.out: %d writes, %d bytes, Duration: %.3f ms, Throughput: %.3f MB/s%n",
                        numWrites, numWrites * messageSize, duration / 1_000_000.0, throughput);
            } else {
                System.err.printf("System.err: %d writes, %d bytes, Duration: %.3f ms, Throughput: %.3f MB/s%n",
                        numWrites, numWrites * messageSize, duration / 1_000_000.0, throughput);
            }
        }
    }
}