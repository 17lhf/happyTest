package com.basic.happytest.modules.multiThread;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.*;

/**
 * 带返回值的线程测试
 * @author : lhf
 */

class CallableImplTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        CallableImpl callableImpl = new CallableImpl();
        FutureTask<String> future = new FutureTask<>(callableImpl);
        System.out.println("1、begin run " + new Date());
        future.run();
        System.out.println("3、result: " + future.get() + " " + new Date()); // 这里会等待线程执行完成
        System.out.println("4、end " + new Date());
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        System.out.println("1、begin run " + new Date());
        Future<String> future = executorService.submit(new CallableImpl());
        System.out.println("1.1、do something " + new Date());
        executorService.shutdown();
        System.out.println("1.2、isShutdown? " + executorService.isShutdown()); // true
        System.out.println("1.3、isTerminated? " + executorService.isTerminated()); // false
        // Future<String> future2 = executorService.submit(new CallableImpl()); // java.util.concurrent.RejectedExecutionException
        System.out.println("3、result: " + future.get() + " " + new Date()); // 这里会等待线程执行完成
        System.out.println("4、end " + new Date());
        executorService.shutdown();
        System.out.println("5、isShutdown? " + executorService.isShutdown()); // true
        System.out.println("1.3、isTerminated? " + executorService.isTerminated()); // true
    }
}