package com.basic.happytest.modules.exploreJVM;

import com.basic.happytest.modules.unboundedWildcardsAndGenerics.Box;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 摸索JVM的一些内容
 * @author lhf
 * 参考： https://blog.csdn.net/briblue/article/details/54973413
 */

public class ExploreJVM {
    private Logger logger = LoggerFactory.getLogger(ExploreJVM.class);

    @Test
    public void testJVM(){
        // 以下这些路径，其实就是这三个类加载器将会查询的环境属性，然后进行资源的加载
        // BootStrap ClassLoader 主要加载核心库 order = 1  用c/c++编写。以下将输出其加载的文件列表。
        System.out.println(System.getProperty("sun.boot.class.path"));
        // Extension ClassLoader 扩展的类加载器 order = 2。 以下将输出其加载的文件列表。
        System.out.println(System.getProperty("java.ext.dirs"));
        // App ClassLoader 当前的应用的类加载器 order = 3。以下将输出其加载的文件列表。
        System.out.println(System.getProperty("java.class.path"));

        // 双亲委托
        ClassLoader classLoader = Box.class.getClassLoader();
        System.out.println("Box's class loader is: " + classLoader); // AppClassLoader
        System.out.println("Box's class loader's parent is: " + classLoader.getParent()); // ExtClassLoader（AppClassLoader的父加载器）
        System.out.println("Box's class loader's parent's parent is: " + classLoader.getParent().getParent()); // null

        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println("String's class loader is: " + classLoader1); // null
        // 其实int、String这些基础类是由BootStrap ClassLoader加载的
    }
}
