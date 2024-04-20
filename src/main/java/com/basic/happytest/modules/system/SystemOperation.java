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

    /**
     * 打印工程根目录绝对路径（即 pom.xml 所在的目录）
     */
    public static void printProjectRootDir() {
        System.out.println(System.getProperty("user.dir"));
    }

    /**
     * 打印当前可用于 Java 虚拟机的处理器数
     * 取决于运行的设备，会受到超线程的影响 <br />
     * 对于创建线程池，设计最佳性能会用到 <br />
     */
    public static void printAvailableProcessors() {
        // 在虚拟机的特定调用期间，此值可能会更改。因此，对可用处理器数敏感的应用程序应偶尔轮询此属性，并适当调整其资源使用情况。
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
