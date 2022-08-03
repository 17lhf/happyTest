package com.basic.happytest.modules.subProcess;

import java.io.*;
import java.util.List;

/**
 * 关于创建子进程执行外部程序或命令的方法集合<br/>
 * 参考文章：https://blog.csdn.net/le_17_4_6/article/details/104419513
 * @author lhf
 */

public class ExecuteProcess {

    /**
     * 普通的执行子进程
     * @param cmd 要被执行的子程序命令
     * @param params 子程序运行过程中，向其传入的参数
     */
    public static void execCmd(List<String> cmd, String params) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        // 默认是false, 表示错误流和标准输出流是独立的
        // 一般是改成true, 这样的话错误流会输出到标准输出流
        processBuilder.redirectErrorStream(true);
        // 创建子进程
        Process process = processBuilder.start();
        // 因为子进程的所有io都将依赖于父进程，所以如果子进程阻塞，则父进程可能会陷入一直等待的情况，所以需要一个子进程监控线程进行决策判定
        // 获取子进程的输出流
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        // 获取子进程的输入流
        OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
        // 向子进程输入数据
        osw.write(params);
        osw.flush();
        osw.close();
        // 获取子进程的输出数据
        String res;
        do{
            res = br.readLine();
            if(res != null) {
                System.out.println("SubProcess output data: " + res);
            }
        } while (res != null);
        br.close();
        // 查看子进程结束时的退出值
        System.out.println("SubProcess exit value: " + process.exitValue());
        // 子进程调用完毕，进行销毁
        process.destroy();
        // 查看子进程状态
        System.out.println("SubProcess is alive? " + process.isAlive());
    }

    /**
     * 执行可能会超时应答的子进程
     * @param cmds 要被执行的子程序命令
     * @param timeLimit 认为子进程不应该超过的应答时长，单位：ms
     * @throws IOException 异常
     */
    public static void execTimeoutCmd(List<String> cmds, long timeLimit) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(cmds);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        // 将子进程放入到线程中运行，然后最多等待一定的时间
        ProcessMonitor processMonitor = new ProcessMonitor(process);
        processMonitor.start();
        try {
            long begin = System.currentTimeMillis();
            // join是最多等待n毫秒，sleep是固定等待n毫秒
            // 因为processMonitor线程存粹就是在与子进程交互，所以其实这个时间也就是最多的交互时间，往往就是最长的等待子进程响应时间
            processMonitor.join(timeLimit);
            if (processMonitor.isComplete()){
                System.out.println("SubProcess has complete gracefully, result is: " + processMonitor.getResult());
            } else {
                long end = System.currentTimeMillis();
                System.out.println("SubProcess execution error or timed out! We have waited " + (end - begin) + " millisecond");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 最后要记得销毁子进程
            process.destroy();
            processMonitor.interrupt();
        }
    }
}
