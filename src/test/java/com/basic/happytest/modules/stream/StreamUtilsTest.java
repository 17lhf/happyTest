package com.basic.happytest.modules.stream;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Stream 流的相关测试
 * @author : lhf
 */

class StreamUtilsTest {

    @Test
    public void testStream() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        // filter 过滤，count 统计
        System.out.println(strings.stream().filter(v -> !v.isEmpty()).count());
        // collect 收集
        System.out.println(strings.stream().filter(v -> !v.isEmpty()).collect(Collectors.joining()));
        // map 转换
        System.out.println(strings.stream().map(v -> v + ",").collect(Collectors.joining()));
        // limit 限制流的数量
        System.out.println(strings.stream().limit(3).collect(Collectors.joining()));
        // parallelStream 并行流(处理过程并行)
        strings.parallelStream().forEach(System.out::println);
        // forEach 遍历流
        Random random = new Random();
        random.ints().limit(3).forEach(System.out::println);
        // sorted 排序
        random.ints().limit(3).sorted().forEach(System.out::println);
    }

    @Test
    void objToMap() {
        List<Obj2MapClass> objList = Arrays.asList(
                new Obj2MapClass("one", "first"),
                new Obj2MapClass("two", "second")
        );
        Map<String, Obj2MapClass> stringObj2MapClassMap = StreamUtils.objToMap(objList);
        System.out.println(JSON.toJSONString(stringObj2MapClassMap.get("one")));
        System.out.println(JSON.toJSONString(stringObj2MapClassMap.get("two")));
    }
}