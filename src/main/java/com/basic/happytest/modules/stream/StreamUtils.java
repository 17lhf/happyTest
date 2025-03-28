package com.basic.happytest.modules.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stream 流的相关工具 <br />
 * Stream 是 Java8 新增的特性
 * @author : lhf
 */

public class StreamUtils {

    /**
     * 将对象列表转换为 Map
     * @param objList 对象列表
     * @return Map
     * @param <T> Key的类型
     * @param <V> Value的类型
     */
    public static <T, V> Map<T, V> objToMap(final List<? extends Obj2MapInterface<T, V>> objList) {
        return objList.stream().collect(Collectors.toMap(Obj2MapInterface::obtainKey, Obj2MapInterface::obtainValue));
    }
}
