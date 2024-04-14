package com.basic.happytest.modules.objectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串转列表工具类
 * @author : lhf
 */

public class StrListTransformUtil {

    /**
     * 默认分隔符
     */
    private static final String DEFAULT_SEPARATOR;
    /**
     * 默认忽略字符串
     */
    private static final List<String> DEFAULT_IGNORE_STR;

    static {
        DEFAULT_SEPARATOR = ",";
        DEFAULT_IGNORE_STR = new ArrayList<>(2);
        DEFAULT_IGNORE_STR.add("\\["); // list 转 Str 默认会带有 [ 开头
        DEFAULT_IGNORE_STR.add("\\]"); // list 转 Str 默认会带有 ] 结尾
        DEFAULT_IGNORE_STR.add("\\s");
    }

    /**
     * 字符串转列表（默认移除空白符、[、]，用逗号做分隔符）
     * @param str 待转换的字符串
     * @param elementType 列表元素类型(仅支持：Integer、Long、Double、Float、Short、Boolean、String)
     * @return 列表
     * @param <T> 元素类型
     */
    public static<T> List<T> str2List(String str, Class<T> elementType) {
        if (str == null) {
            throw new NullPointerException();
        }
        List<T> list = new ArrayList<>();
        if (!str.isEmpty()) {
            String newStr = str;
            for (String ignoreStr : DEFAULT_IGNORE_STR) {
                newStr = newStr.replaceAll(ignoreStr, "");
            }
            String[] strArray = newStr.split(DEFAULT_SEPARATOR);
            for (String s : strArray) {
                if(elementType.equals(Integer.class)) {
                    list.add(elementType.cast(Integer.valueOf(s)));
                } else if(elementType.equals(Long.class)) {
                    list.add(elementType.cast(Long.valueOf(s)));
                } else if(elementType.equals(Double.class)) {
                    list.add(elementType.cast(Double.valueOf(s)));
                } else if(elementType.equals(Float.class)) {
                    list.add(elementType.cast(Float.valueOf(s)));
                } else if(elementType.equals(Short.class)) {
                    list.add(elementType.cast(Short.valueOf(s)));
                } else if(elementType.equals(Boolean.class)) {
                    list.add(elementType.cast(Boolean.valueOf(s)));
                } else if(elementType.equals(String.class)) {
                    list.add(elementType.cast(s));
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
        return list;
    }
}
