package com.basic.happytest.modules.subProcess;

import com.basic.happytest.modules.fileIO.FileIO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于运行子进程的测试案例
 * @author lhf
 */

class ExecuteProcessTest {
    /**
     * 能输出，能接受输入的程序
     */
    private static final String IN_OUT_NORMAL_PROCESS_PROCESS1 = "/static/SubProcesses/test1.exe";
    /**
     * 先输出一句，然后延迟5s后输出后面两句
     */
    private static final String IN_OUT_NORMAL_PROCESS_PROCESS2 = "/static/SubProcesses/testForJava2.exe";

    // 寻常的与子进程交互的测试
    @Test
    void execCmd() throws IOException {
        List<String> cmds = new ArrayList<>();
        String subProcessPath = FileIO.getResourceAbsolutePath(IN_OUT_NORMAL_PROCESS_PROCESS1);
        cmds.add(subProcessPath);
        cmds.add("123");
        cmds.add("One");
        cmds.add("一二三");
        ExecuteProcess.execCmd(cmds, "abc");
    }

    // 设置子进程交互最长时间的测试
    @Test
    void execTimeoutCmd() throws IOException {
        List<String> cmds = new ArrayList<>();
        String subProcessPath = FileIO.getResourceAbsolutePath(IN_OUT_NORMAL_PROCESS_PROCESS2);
        cmds.add(subProcessPath);
        // 超时测试
        ExecuteProcess.execTimeoutCmd(cmds, 4000);
        // 不超时测试
        // ExecuteProcess.execTimeoutCmd(cmds, 7000);
    }
}