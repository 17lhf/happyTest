package com.basic.happytest.modules.fileIO;

import com.basic.happytest.modules.randomUtils.GenChineseName;
import com.basic.happytest.modules.randomUtils.GenNumber;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * csv文件操作测试
 * @author
 */

class CsvUtilsTest {
    // 存储生成的文件的路径
    static String STORE_PATH = "/static/fileIO/csv/";

    @Test
    void write() throws Exception {
        List<String> titles = new ArrayList<>();
        titles.add("班级");
        titles.add("学生");
        titles.add("成绩");
        List<List<String>> content = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> list = new ArrayList<>();
            list.add("一班");
            list.add(GenChineseName.getChineseName());
            list.add(String.valueOf(GenNumber.getIntegerNumber(0, true, 200, true) * 0.5));
            content.add(list);
        }
        String filePath = FileIO.getResourceAbsolutePath(STORE_PATH) + "/test1.csv";
        CsvUtils.write(titles, content, filePath);
        // FileIO.deleteFile(filePath);
    }
}