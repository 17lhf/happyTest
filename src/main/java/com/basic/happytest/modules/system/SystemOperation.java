package com.basic.happytest.modules.system;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

/**
 * 一些关于系统的操作
 * @author lhf
 */

public class SystemOperation {

    /**
     * 检测当前运行环境是否是Windows环境
     * @return true-是，false-否
     */
    public static boolean isWinSystem() {
        String system = System.getProperty("os.name");
        System.out.println("系统名： " + system);
        // system.toLowerCase().startsWith("linux"); // 检测Linux环境
        return system.toLowerCase().startsWith("win");
    }

    /**
     * 打印Spring和SpringBoot的版本
     */
    public static void printSpringVersion() {
        System.out.println("Spring version: " + SpringVersion.getVersion());
        System.out.println("Spring Boot version: " + SpringBootVersion.getVersion());
    }
}
