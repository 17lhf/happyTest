package com.basic.happytest.modules.fileIO;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * 一系列的关于文件和流的操作
 * @author lhf
 */

public class FileIO {

    /**
     * 依据相对路径获取绝对路径
     * @param path resource文件夹底下的相对路径
     * @return 绝对路径
     * @throws IOException 异常
     */
    public static String getAbsolutePath(String path) throws IOException {
        String filePath = (new ClassPathResource(path)).getFile().getPath();
        System.out.println("文件路径为： " + filePath);
        return filePath;
    }

    /**
     * 比较两个文件内容是否相同
     * @param file1Path 文件1的路径
     * @param file2Path 文件2的路径
     * @param ignoreEOL 是否忽略换行符，true-忽略，false-不忽略
     * @return true-相同，false-不同
     * @throws IOException 异常
     */
    public static boolean compareTwoFile(String file1Path, String file2Path, boolean ignoreEOL) throws IOException {
        Reader reader1 = new BufferedReader(new FileReader(file1Path));
        Reader reader2 = new BufferedReader(new FileReader(file2Path));
        // 比较是否相同
        boolean isEqual;
        if (ignoreEOL) {
            // 这种会忽略换行符 \n \r ，避免不同平台文件的对比问题
            isEqual = IOUtils.contentEqualsIgnoreEOL(reader1, reader2);
        } else {
            // 这种不会忽略换行符 \n \r
            isEqual = IOUtils.contentEquals(reader1, reader2);
        }
        System.out.println("This two file content is equal? " + isEqual);
        return isEqual;
    }

    /**
     * 读取指定文件的内容并返回(最大允许文件大小为20kb)
     * @param filePath 文件绝对路径
     * @return 文件内容（字符串形式）
     */
    public static String getFileContent(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()){
            throw new FileNotFoundException();
        }
        String encoding = "UTF-8";
        long fileLen = file.length();
        System.out.println("File length is: " + fileLen);
        if(fileLen > 20 * 1024) {
            throw new OutOfMemoryError();
        }
        byte[] filecontent = new byte[(int) fileLen];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(filecontent, encoding);
    }
}
