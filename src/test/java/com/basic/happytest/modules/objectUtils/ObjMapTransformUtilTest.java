package com.basic.happytest.modules.objectUtils;

import com.basic.happytest.modules.objectUtils.Objs.TestClass;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象和map键值对相互转换的测试
 * @author lhf
 */

class ObjMapTransformUtilTest {

    @Test
    void obj2Map() throws Exception {
        TestClass testClass = new TestClass();
        testClass.pubFloat = 3.1f;
        Map<String, Object> map = ObjMapTransformUtil.Obj2Map(testClass);
        System.out.println(map);
    }

    @Test
    void map2Obj() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("num", 1);
        map.put("pubFloat", 2.1f);
        TestClass testClass = ObjMapTransformUtil.map2Obj(map, TestClass.class);
        System.out.println(ObjMapTransformUtil.Obj2Map(testClass));
    }
}