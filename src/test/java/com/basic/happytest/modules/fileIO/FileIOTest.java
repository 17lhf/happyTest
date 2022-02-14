package com.basic.happytest.modules.fileIO;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件和流相关内容的测试
 * @author lhf
 */

class FileIOTest {

    static String EQUAL_FILE1 = "static/fileIO/equalFile1.txt";
    static String EQUAL_FILE2 = "static/fileIO/equalFile2.txt";
    static String NOT_EQUAL_FILE = "static/fileIO/notEqualFile.txt";
    static String WAIT_DELETE_FILE = "static/fileIO/wait2delete.txt";

    @Test
    void compareTwoFile() throws IOException {
        FileIO.compareTwoFile(FileIO.getAbsolutePath(EQUAL_FILE1), FileIO.getAbsolutePath(EQUAL_FILE2), false);
        FileIO.compareTwoFile(FileIO.getAbsolutePath(NOT_EQUAL_FILE), FileIO.getAbsolutePath(EQUAL_FILE2), false);
    }

    @Test
    void getFileContent() throws Exception {
        System.out.println(FileIO.getFileContent(FileIO.getAbsolutePath(EQUAL_FILE1)));
    }

    @Test
    void deleteFile() throws Exception {
        String absolutePath = FileIO.getAbsolutePath(WAIT_DELETE_FILE);
        System.out.println(FileIO.getFileContent(absolutePath));
        FileIO.deleteFile(absolutePath);
        File file = new File(absolutePath);
        if(file.exists()){
            System.out.println("file exists");
        } else {
            System.out.println("file is deleted");
        }
    }
}