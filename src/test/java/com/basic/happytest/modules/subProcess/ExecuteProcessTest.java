package com.basic.happytest.modules.subProcess;

import com.basic.happytest.modules.fileIO.FileIO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExecuteProcessTest {

    private static String IN_OUT_NORMAL_PROCESS_PROCESS = "static/SubProcesses/test1.exe";

    @Test
    void execCmd() throws IOException {
        List<String> cmds = new ArrayList<>();
        String subProcessPath = FileIO.getAbsolutePath(IN_OUT_NORMAL_PROCESS_PROCESS);
        cmds.add(subProcessPath);
        cmds.add("123");
        cmds.add("One");
        cmds.add("一二三");
        ExecuteProcess.execCmd(cmds, "abc");
    }
}