package com.basic.happytest.modules.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemOperationTest {

    @Test
    void isWinSystem() {
        System.out.println("是否是Windows系统？ " + SystemOperation.isWinSystem());
    }
}