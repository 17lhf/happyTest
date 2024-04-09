package com.basic.happytest.modules.fileIO;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;

/**
 * 文件和流相关内容的测试
 * @author lhf
 */

class FileIOTest {

    static String EQUAL_FILE1 = "/static/fileIO/equalFile1.txt";
    static String EQUAL_FILE2 = "/static/fileIO/equalFile2.txt";
    static String NOT_EQUAL_FILE = "/static/fileIO/notEqualFile.txt";
    static String WAIT_DELETE_FILE = "/static/fileIO/wait2delete.txt";
    /**
     * 两个resource相同相对路径下都有放置，用来测试看获取绝对路径时会加载哪个文件
     */
    static String LOAD_TEST_FILE = "/static/fileIO/loadTestFile1.txt";

    @Test
    void compareTwoFile() throws IOException {
        FileIO.compareTwoFile(FileIO.getResourceAbsolutePath(EQUAL_FILE1), FileIO.getResourceAbsolutePath(EQUAL_FILE2), false);
        FileIO.compareTwoFile(FileIO.getResourceAbsolutePath(NOT_EQUAL_FILE), FileIO.getResourceAbsolutePath(EQUAL_FILE2), false);
    }

    @Test
    void getFileContent() throws Exception {
        System.out.println(FileIO.getFileContent(FileIO.getResourceAbsolutePath(EQUAL_FILE1)));
    }

    @Test
    void deleteFile() throws Exception {
        String absolutePath = FileIO.getResourceAbsolutePath(WAIT_DELETE_FILE);
        System.out.println(FileIO.getFileContent(absolutePath));
        FileIO.deleteFile(absolutePath);
        File file = new File(absolutePath);
        if(file.exists()){
            System.out.println("file exists");
        } else {
            System.out.println("file is deleted");
        }
    }

    @Test
    void pushBackIO() throws IOException {
        FileIO.pushBackIO();
    }

    /**
     * 各种加载路径的尝试
     */
    @Test
    void getAbsolutePath() throws IOException {
        //    com.basic.happytest.modules.fileIO.FileIO
        System.out.println("0:" + FileIO.class.getName());
        //    /target/test-classes/com/basic/happytest/modules/fileIO/
        System.out.println("1:" + FileIO.class.getResource(""));
        //    /target/test-classes/com/basic/happytest/modules/fileIO/
        System.out.println("2:" + FileIO.class.getResource("/com/basic/happytest/modules/fileIO/"));
        //    /target/test-classes/
        System.out.println("3:" + FileIO.class.getResource("/"));
        //    /target/test-classes/static/attachments/attach1.txt
        System.out.println("4:" + FileIO.class.getResource("/static/attachments/attach1.txt"));
        // 实测发现，class.getResource会先利用 AppClassLoader以及双亲 去尝试加载，加载不到后由 URLClassLoader 去尝试加载
        //    /target/test-classes/
        System.out.println("5:" + URLClassLoader.getSystemResource(""));
        //    /target/test-classes/static/fileIO/equalFile1.txt
        System.out.println("6:" + URLClassLoader.getSystemResource("static/fileIO/equalFile1.txt"));
        //    null   看源码显示，似乎是因为查询路径就是 baseDir/target/test-classes/ ，然后如果资源路径再加上斜杠开头会导致路径不存在
        System.out.println("7:" + URLClassLoader.getSystemResource(EQUAL_FILE1));

        System.out.println("--------------------------------------------------------------------------------------");

        //    sun.misc.Launcher$AppClassLoader
        System.out.println("8:" + FileIO.class.getClassLoader().getClass().getName());
        // class.getClassLoader().getResource() 本质上是 class.getResource() 的内部实现，相当于去掉了传入路径处理和判定非空的环节
        // 所以这里也是先利用 AppClassLoader以及双亲 去尝试加载，加载不到后由 URLClassLoader 去尝试加载
        //    /target/test-classes/
        System.out.println("9:" + FileIO.class.getClassLoader().getResource(""));
        //    /target/test-classes/static/fileIO/equalFile1.txt
        System.out.println("10:" + FileIO.class.getClassLoader().getResource("static/fileIO/equalFile1.txt"));
        //    null
        System.out.println("11:" + FileIO.class.getClassLoader().getResource("/"));

        System.out.println("--------------------------------------------------------------------------------------");

        // 这里入参有无斜杠开头都一样，内部都会将其移除
        // 这里如果是获取文件夹路径，则最后得到的路径末尾无斜杠（这是因为利用的是File对象获取路径）
        // 通过看源码发现，其实兜兜转转，最后其实还是 classLoader 在处理
        //    \target\test-classes
        System.out.println("12:" + (new ClassPathResource("")).getFile().getPath());
        //    \target\test-classes
        System.out.println("13:" + (new ClassPathResource("/")).getFile().getPath());
        //    \target\classes\static\fileIO\equalFile1.txt
        System.out.println("14:" + (new ClassPathResource(EQUAL_FILE1)).getFile().getPath());
        //    \target\classes\static\fileIO\equalFile1.txt
        System.out.println("15:" + (new ClassPathResource("static/fileIO/equalFile1.txt")).getFile().getPath());
        //    \target\test-classes\static\fileIO
        System.out.println("16:" + (new ClassPathResource("static/fileIO/")).getFile().getPath());
        //    \happyTest\target\test-classes\static\fileIO
        System.out.println("17:" + (new ClassPathResource("static/fileIO")).getFile().getPath());

        System.out.println("--------------------------------------------------------------------------------------");

        //  如果两个resource相同相对路径下都有放置一样命名的文件，在这里似乎都是会获取 test-classes 目录下的文件（因为这里是单元测试，所以优先加载测试编译目录？）
        //    /target/test-classes/static/fileIO/loadTestFile1.txt
        System.out.println("18:" + FileIO.class.getResource(LOAD_TEST_FILE));
        //    /target/test-classes/static/fileIO/loadTestFile1.txt
        System.out.println("19:" + URLClassLoader.getSystemResource("static/fileIO/loadTestFile1.txt"));
        //    \target\test-classes\static\fileIO\loadTestFile1.txt
        System.out.println("20:" + (new ClassPathResource(LOAD_TEST_FILE)).getFile().getPath());
    }
}