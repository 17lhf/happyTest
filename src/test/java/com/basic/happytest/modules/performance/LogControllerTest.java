package com.basic.happytest.modules.performance;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

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

}