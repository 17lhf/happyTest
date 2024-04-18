package com.basic.happytest.modules.multiThread;

import java.util.concurrent.Callable;

/**
 * 实现Callable接口的实现类（带返回值）
 * @author : lhf
 */

public class CallableImpl implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("2、begin sleep");
        Thread.sleep(3000);
        return "CallableImpl sleep over.";
    }
}
