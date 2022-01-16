package com.basic.happytest.modules.jsonTranslate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.basic.happytest.modules.lombokExplore.ClassA;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestJsonTranslate {

    /**
     * todo
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
        String classAListJSON = JSON.toJSONString(classAList);
        List<ClassA> classAList1 = JSON.parseArray(classAListJSON, ClassA.class);
        System.out.println("JSON.parseArray object: " + classAList1.get(0).toString());
        System.out.println("JSON.parseArray list length: " + classAList1.size());
    }
}
