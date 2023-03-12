package com.basic.happytest.modules.system;

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
}
