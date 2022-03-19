package com.basic.happytest.modules.jsonTranslate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.basic.happytest.modules.lombokExplore.ClassA;
import com.basic.happytest.modules.lombokExplore.model.UseLombokDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestJsonTranslate {

    /**
     * JSONObject继承自JSON,操作起来不同，实际效果其实都一样
     * 似乎并没有什么值得深究的......
     */
    @Test
    public void testJsonTranslate(){

        System.out.println("--------------to string--------------");
        ClassA classA = new ClassA();
        String classAJSON1 = JSONObject.toJSONString(classA);
        System.out.println("JSONObject.toJSONString: " + classAJSON1);
        String classAJSON2 = JSON.toJSONString(classA);
        System.out.println("JSON.toJSONString: " + classAJSON2);

        System.out.println("--------------to JSON----------------");
        JSONObject json = (JSONObject) JSONObject.toJSON(classA);
        System.out.println("(JSONObject) JSONObject.toJSON: " + json.get("i"));
        JSONObject jsonObject = (JSONObject) JSONObject.parse(classAJSON1);
        System.out.println("(JSONObject JSONObject.parse: " + jsonObject.get("i"));

        System.out.println("-----------to object------------------");
        ClassA classA3 = JSONObject.parseObject(classAJSON1, ClassA.class);
        System.out.println("JSONObject.parseObject: " + classA3.toString());
        ClassA classA4 = JSON.parseObject(classAJSON1, ClassA.class);
        System.out.println("JSON.parseObject: " + classA4.toString());

        System.out.println("-------------to object array------------");
        List<ClassA> classAList = new ArrayList<>(1);
        classAList.add(classA);
        classAList.add(classA4);
        String classAListJSON = JSON.toJSONString(classAList);
        List<ClassA> classAList1 = JSON.parseArray(classAListJSON, ClassA.class);
        System.out.println("JSON.parseArray object[0]: " + classAList1.get(0).toString());
        System.out.println("JSON.parseArray object[1]: " + classAList1.get(1).toString());
        System.out.println("JSON.parseArray list length: " + classAList1.size());
    }

    /**
     * 测试fastjson和jackson对对象进行反序列化时的差异
     */
    @Test
    public void testJson(){
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("aInt","0");
        hashMap.put("oneInt","0");
        hashMap.put("aStr","abc");
        hashMap.put("oneStr","abc");

        UseLombokDto useLombokDto = JSONObject.parseObject(JSONObject.toJSONString(hashMap), UseLombokDto.class);
        System.out.println("fastjson: ");
        System.out.println("aInt: " + useLombokDto.getAInt());
        System.out.println("oneInt: " + useLombokDto.getOneInt());
        System.out.println("aStr: " + useLombokDto.getAStr());
        System.out.println("oneStr: " + useLombokDto.getOneStr());
        System.out.println();

        ObjectMapper objectMapper = new ObjectMapper();
        // 抛异常：java.lang.IllegalArgumentException: Unrecognized field "aInt" (class com.basic.happytest.modules.lombokExplore.model.UseLombokDto),
        // not marked as ignorable (4 known properties: "astr", "oneInt", "oneStr", "aint"])
        UseLombokDto useLombokDto1 = objectMapper.convertValue(hashMap, UseLombokDto.class);
        System.out.println("jackson: ");
        System.out.println("aInt: " + useLombokDto.getAInt());
        System.out.println("oneInt: " + useLombokDto.getOneInt());
        System.out.println("aStr: " + useLombokDto.getAStr());
        System.out.println("oneStr: " + useLombokDto.getOneStr());
        System.out.println();
    }
}
