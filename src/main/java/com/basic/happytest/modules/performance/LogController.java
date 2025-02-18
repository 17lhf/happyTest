package com.basic.happytest.modules.performance;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在生产环境中，禁止使用System.out、System.err、e.printStackTrace()，此操作将严重影响高并发场景的性能
 * 此处为进行实验观察性能影响情况
 * @author : lhf
 */

@RestController
@RequestMapping("/log")
public class LogController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LogController.class);

    private static final int LOOP_COUNT = 100;

    @GetMapping("/system-out")
    public String logSystemOut() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            System.out.println("hello world");
        }
        long endTime = System.currentTimeMillis();
        return "Logged messages in " + (endTime - startTime) + " ms";
    }

    @GetMapping("/system-err")
    public String logSystemErr() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            System.err.println("hello world");
        }
        long endTime = System.currentTimeMillis();
        return "Logged messages in " + (endTime - startTime) + " ms";
    }

    @GetMapping("/info")
    public String logInfo() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            logger.info("hello world");
        }
        long endTime = System.currentTimeMillis();
        return "Logged messages in " + (endTime - startTime) + " ms";
    }
}
