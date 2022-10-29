package com.basic.happytest.modules.fileIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
        try (FileInputStream in = new FileInputStream(file)){
            in.read(filecontent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(filecontent, encoding);
    }

    /**
     * 依据提供的路径，生成文件夹
     * @param folderPath 要生成的文件夹的绝对路径
     * @return true-创建成功，false-创建失败
     */
    public static boolean createFolder(String folderPath) {
        boolean success = true;
        if(StringUtils.isBlank(folderPath)) {
            System.out.println("文件夹路径为空，无法创建新文件夹");
            success = false;
        } else {
            File folder = new File(folderPath);
            if(!folder.mkdirs()) {
                System.out.println("文件夹创建失败，可能文件夹已存在");
                success = false;
            }
        }
        return success;
    }

    /**
     * 删除指定路径的文件, 若找不到则抛异常
     * @param filePath 文件绝对路径
     * @return 是否成功
     * @throws Exception 异常
     */
    public static boolean deleteFile(String filePath) throws Exception {
        System.out.println("开始删除文件， 文件路径为： " + filePath);
        File file = new File(filePath);
        if(file.exists()){
            return file.delete();
        }
        throw new FileNotFoundException();
    }

    /**
     * 关于回退流
     * @throws IOException 异常
     */
    public static void pushBackIO() throws IOException {
        String str = "hello,world!";
        PushbackInputStream push = null; // 声明回退流对象
        ByteArrayInputStream bat = null; // 声明字节数组流对象
        bat = new ByteArrayInputStream(str.getBytes());
        push = new PushbackInputStream(bat); // 创建回退流对象，将拆解的字节数组流传入。默认的缓冲区大小为1
        int temp = 0;
        boolean isOut = false;
        while ((temp = push.read()) != -1) { // push.read()逐字节读取存放在temp中，如果读取完成返回-1
            if (temp == ',' && !isOut) { // 判断读取的是否是逗号
                push.unread('!'); // 可以将temp放入，表示回到temp的位置。也可以输入其他数据，然后等待后续被打印出来。
                System.out.print("(回退" + (char) temp + ") "); // 输出回退的字符
                isOut = true;
            } else {
                System.out.print((char) temp); // 否则输出字符
            }
        }
        // 结果： hello(回退,) !world
    }
}
