package com.basic.happytest.modules.fileIO;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * CSV文件操作工具类
 * @author lhf
 */

public class CsvUtils {

    private static final String COMMA = ",";

    /**
     * 写入csv数据内容
     * @param titles 标题
     * @param content 内容，每个list项表示一行数据
     * @param filePath 文件的绝对路径
     */
    public static void write(List<String> titles, List<List<String>> content, String filePath) throws Exception {
        File file = new File(filePath);
        if(!file.createNewFile()) {
            System.out.println("CSV文件已存在，新建文件失败");
            throw new Exception();
        }
        try (FileWriter writer = new FileWriter(file)){
            // 标识为UTF-8编码，否则中文会变成乱码
            // Excel默认并不是以UTF-8来打开文件，所以在csv开头加入BOM，告诉Excel打开文件时使用utf-8的编码方式
            writer.write(new String(new byte[]{( byte ) 0xEF ,( byte ) 0xBB ,( byte ) 0xBF }));
            // 写入列名
            writer.write(StringUtils.join(titles, COMMA) + "\n");
            // 写入各行内容
            for (List<String> contentRows : content) {
                writer.write(StringUtils.join(contentRows, COMMA) + "\n");
            }
        } catch (Exception e) {
            System.out.println("CSV文件内容写入失败！");
            throw new Exception();
        }
    }
}
