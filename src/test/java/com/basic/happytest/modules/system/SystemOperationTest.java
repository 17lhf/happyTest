package com.basic.happytest.modules.system;

import org.junit.jupiter.api.Test;

class SystemOperationTest {

    @Test
    void isWinSystem() {
        System.out.println("是否是Windows系统？ " + SystemOperation.isWinSystem());
    }

    @Test
    void printSpringVersion() {
        SystemOperation.printSpringVersion();
    }

    @Test
    void printProjectRootDir() {
        SystemOperation.printProjectRootDir();
    }

    @Test
    void printAvailableProcessors() {
        SystemOperation.printAvailableProcessors();
    }
}