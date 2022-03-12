package com.basic.happytest.modules.subProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 关于对子进程运行超时监控的进程
 * @author lhf
 */

public class ProcessMonitor extends Thread{

    // 要被监控的子进程
    private final Process process;

    /**
     * 子进程是否正常完成，true-正常完成，false-未完成
     */
    private boolean isComplete = false;

    /**
     * 获取子进程的输出
     */
    private StringBuilder result;

    public ProcessMonitor(Process process){
        this.process = process;
        this.result = new StringBuilder();
    }

    @Override
    public void run(){
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String res;
            do{
                // 如果子进程没有响应，这里会阻塞
                res = br.readLine();
                if(res != null) {
                    System.out.println("SubProcess output data: " + res);
                    result.append(res);
                }
            } while (res != null);
            // 能到达这里，说明子线程的输出已经读取完毕，是正常结束
            isComplete = true;
        } catch (IOException e){
            // 如果出现异常，这里是直接结束
            // 当然，也可以做一些其他的操作
            Thread.currentThread().interrupt();
        } finally {
            // 无论是否出现异常，都要结束流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getResult() {
        return result.toString();
    }

    public Boolean isComplete() {
        return this.isComplete;
    }
}
