package com.basic.happytest.modules.textProcessing;

/**
 * 文本处理工具
 * @author lhf
 */

public class TextProcessingUtils {

    /**
     * 将字符串中的"\\n"转换为“换行符”(通常是针对带有换行符的数据经由json字符串自动转换后，需要逆转换回原来的格式时使用)
     * @param data 待转换的字符串
     * @return 转换后的字符串
     */
    public static String replaceLineBreak(String data) {
        // “\\\\”，第一个斜杠是转义符，第二个斜杠是斜杠本身，第三个斜杠是转义符，第四个斜杠是斜杠本身
        // "\\\\\\\\n"中前四个斜杠表示匹配字符串中的一个斜杠，后面四个斜杠表示换行符的斜杠
        return data.replaceAll("\\\\\\\\n", "\n");
    }
}
