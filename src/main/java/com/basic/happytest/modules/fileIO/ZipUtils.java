package com.basic.happytest.modules.fileIO;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip压缩和解压工具类
 * @author lhf
 * 参考：Java实现解压缩文件和文件夹：https://www.cnblogs.com/luciochn/p/14515029.html
 */

public class ZipUtils {

    /**
     * 压缩一整个文件夹 或 单个文件（压缩文件夹的话按原有的文件树进行压缩）
     * @param srcPath 要被压缩的 文件夹或文件 的绝对路径
     * @param targetPath 压缩后生成的zip文件存放绝对路径
     */
    public static void zip(String srcPath, String targetPath) throws Exception {
        File srcFile = new File(srcPath);
        if(!srcFile.exists()) {
            throw new Exception("要被压缩的文件不存在");
        }
        File targetFile = new File(targetPath);
        if(targetFile.exists()) {
            throw new Exception("已经存在同名目标文件");
        }
        System.out.println("压缩开始");
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(targetFile))) {
            // baseDir传入为空，说明压缩的路径基于的就是被压缩的目录，解压后的文件夹路径起始就是被压缩的文件夹文件名
            compress(srcFile, zipOutputStream, "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("压缩文件夹失败");
        }
        System.out.println("压缩完毕");
    }

    /**
     * 统一的压缩方法
     * @param file 待压缩的文件（可能是单个文件，也可能是一个文件夹）
     * @param zipOutputStream zip输出流
     */
    private static void compress(File file, ZipOutputStream zipOutputStream, String baseDir) throws Exception {
        if(file.isDirectory()){
            zipDir(file, zipOutputStream, baseDir);
        } else {
            zipFile(file, zipOutputStream, baseDir);
        }
    }

    /**
     * 压缩单个文件（内容为空的文件也会被压缩）
     * @param file 单个文件
     * @param zipOutputStream zip输出流
     * @throws Exception 异常
     */
    private static void zipFile(File file, ZipOutputStream zipOutputStream, String baseDir) throws Exception {
        if(!file.exists()){
            return;
        }
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            // 不使用baseDir时的文件路径输入，使用绝对路径的话，压缩包的里面的文件夹路径会是从驱动盘路径开始，如D://ABC//abc.txt
            // ZipEntry zipEntry = new ZipEntry(file.getAbsolutePath());
            ZipEntry zipEntry = new ZipEntry(baseDir + file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            int length;
            byte[] buf = new byte[1024];
            while ((length = fileInputStream.read(buf)) > 0){
                zipOutputStream.write(buf, 0, length);
                buf = new byte[1024];
            }
            zipOutputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("");
        }
    }

    /**
     * 压缩文件夹
     * @param dir 文件夹对象
     * @param zipOutputStream zip输出流
     * @throws Exception 异常
     */
    private static void zipDir(File dir, ZipOutputStream zipOutputStream, String baseDir) throws Exception {
        if(!dir.exists()) {
            return;
        }
        File[] subFiles = dir.listFiles();
        // 将空文件夹也打进压缩包，如果不想打入压缩包，就注释掉处理内容，直接return
        if(subFiles == null || subFiles.length == 0){
            ZipEntry zipEntry = new ZipEntry(baseDir + dir.getName() + "/");
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.closeEntry();
            return;
        }
        for (File file: subFiles) {
            compress(file, zipOutputStream, baseDir + dir.getName() + "/");
        }
    }

    /**
     * 压缩 文件或文件夹内所有文件 到同一层级（即会失去原来的文件数结构）
     * @param srcPath 要被压缩的 文件或文件夹 的绝对路径
     * @param targetPath 生成的zip压缩包存放的绝对路径
     */
    public static void zip2OneFolder(String srcPath, String targetPath) throws Exception {
        File srcFile = new File(srcPath);
        if(!srcFile.exists()) {
            throw new Exception("要被压缩的文件不存在");
        }
        File targetFile = new File(targetPath);
        if(targetFile.exists()) {
            throw new Exception("已经存在同名目标文件");
        }
        System.out.println("压缩开始");
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(targetFile))) {
            compress(srcFile, zipOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("压缩文件夹失败");
        }
        System.out.println("压缩完毕");
    }

    /**
     * 统一的压缩方法
     * @param file 待压缩的文件（可能是单个文件，也可能是一个文件夹）
     * @param zipOutputStream zip输出流
     */
    private static void compress(File file, ZipOutputStream zipOutputStream) throws Exception {
        if(file.isDirectory()){
            zipDir(file, zipOutputStream);
        } else {
            zipFile(file, zipOutputStream);
        }
    }

    /**
     * 压缩单个文件（内容为空的文件也会被压缩）
     * @param file 单个文件
     * @param zipOutputStream zip输出流
     * @throws Exception 异常
     */
    private static void zipFile(File file, ZipOutputStream zipOutputStream) throws Exception {
        if(!file.exists()){
            return;
        }
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            // 不使用baseDir时的文件路径输入，使用绝对路径的话，压缩包的里面的文件夹路径会是从驱动盘路径开始，如D://ABC//abc.txt
            // ZipEntry zipEntry = new ZipEntry(file.getAbsolutePath());
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            int length;
            byte[] buf = new byte[1024];
            while ((length = fileInputStream.read(buf)) > 0){
                zipOutputStream.write(buf, 0, length);
                buf = new byte[1024];
            }
            zipOutputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("");
        }
    }

    /**
     * 压缩文件夹
     * @param dir 文件夹对象
     * @param zipOutputStream zip输出流
     * @throws Exception 异常
     */
    private static void zipDir(File dir, ZipOutputStream zipOutputStream) throws Exception {
        if(!dir.exists()) {
            return;
        }
        File[] subFiles = dir.listFiles();
        // 将各个文件夹也打进压缩包，如果不想打入压缩包，就注释掉处理内容
        ZipEntry zipEntry = new ZipEntry(dir.getName() + "/");
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.closeEntry();
        // 处理各个子文件
        if(subFiles != null && subFiles.length != 0){
            for (File file: subFiles) {
                compress(file, zipOutputStream);
            }
        }
    }

    /**
     * 解压文件
     * @param srcZipFilePath zip压缩包的绝对路径
     * @param targetPath 解压后的文件存放的文件夹的绝对路径
     * @throws Exception 异常
     */
    public static void unzip(String srcZipFilePath, String targetPath) throws Exception {
        File srcZipFile = new File(srcZipFilePath);
        if(!srcZipFile.exists()){
            throw new Exception("压缩包文件不存在");
        }
        File targetFile = new File(targetPath);
        if(!targetFile.exists()) {
            throw new Exception("目标文件夹路径错误");
        }
        System.out.println("开始解压");
        try(ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(srcZipFile))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File file = new File(targetPath + File.separator + zipEntry.getName());
                if(zipEntry.isDirectory()) { // 是文件夹
                    mkdir(file);
                } else { // 是文件
                    mkdir(file.getParentFile());
                    try(BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(bytes)) > 0) {
                            bufferedOutputStream.write(bytes, 0, length);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        throw new Exception("文件生成失败");
                    }
                }
                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("解压失败");
        }
        System.out.println("解压完毕");
    }

    /**
     * 递归创建文件夹及父文件夹
     * @param file 文件夹对象
     * @throws Exception 异常
     */
    private static void mkdir(File file) throws Exception {
        if(null == file || file.exists()){
            return;
        }
        mkdir(file.getParentFile());
        if(!file.mkdir()){
            throw new Exception(file.getName() + "文件夹创建失败");
        }
    }
}
