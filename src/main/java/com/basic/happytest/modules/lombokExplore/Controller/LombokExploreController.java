package com.basic.happytest.modules.lombokExplore.Controller;

import com.basic.happytest.modules.lombokExplore.model.NormalDto;
import com.basic.happytest.modules.lombokExplore.model.UseLombokAndPropertyDto;
import com.basic.happytest.modules.lombokExplore.model.UseLombokDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试lombok细节问题的控制层
 * @author lhf
 * 参考：https://blog.csdn.net/Angry_Mills/article/details/91947928
 * postman提交的body:
 * {
 *   "aInt": 0,
 *   "oneInt": 0,
 *   "aStr": "abc",
 *   "oneStr": "abc"
 * }
 */

@RestController
@RequestMapping("/lombok-explore/")
public class LombokExploreController {

    /**
     * 符合java bean规范的入参实体get/set方法测试接口
     * @param dto 入参数据
     */
    @PostMapping("normal")
    public void normalTest(@RequestBody NormalDto dto){
        System.out.println("Normal lombok dto: ");
        if(dto == null) {
            System.out.println("dto: null");
        } else {
            System.out.println("aInt: " + dto.getaInt());
            System.out.println("oneInt: " + dto.getOneInt());
            System.out.println("aStr: " + dto.getaStr());
            System.out.println("oneStr: " + dto.getOneStr());
        }
        /**
         * 结果示例：
         * Normal lombok dto:
         * aInt: 0
         * oneInt: 0
         * aStr: abc
         * oneStr: abc
         */
    }

    /**
     * 使用lombok的入参实体get/set方法测试接口
     * @param dto 入参数据
     */
    @PostMapping("useLombok")
    public void useLombokTest(@RequestBody UseLombokDto dto){
        System.out.println("Use lombok dto: ");
        if(dto == null) {
            System.out.println("dto: null");
        } else {
            System.out.println("aInt: " + dto.getAInt()); // 正常的aInt变量的get方法应该是getaInt,set方法是setaInt
            System.out.println("oneInt: " + dto.getOneInt());
            System.out.println("aStr: " + dto.getAStr()); // 正常的aStr变量的get方法应该是getaStr,set方法是setaStr
            System.out.println("oneStr: " + dto.getOneStr());
        }
        /**
         * 结果示例：
         * Use lombok dto:
         * aInt: null
         * oneInt: 0
         * aStr: null
         * oneStr: abc
         * 因为lombok的设计者在对首字母小写，第二个字母大写的情况下的get和set方法的命名不符合java标准
         * 这使得mvc（jackson）在将json报文解析后封装进对象时，会找不到匹配变量，从而使得对应的变量数据为空
         * 其实就是jackson解析报文的差异引起数据未能成功注入
         * json解析可看测试：{@link com.basic.happytest.modules.jsonTranslate.TestJsonTranslate#testJson()}
         * 解决方式：
         * 1.在变量上使用@JsonProperty("aInt")
         * 2.不用lombok
         * 3.修改默认json解析器，不用jackson序列化，改用fastjson
         * 4.修改变量命名
         * 5.前端传参时，改参数名，把aInt命名成aint，aStr命名成astr，后端就能收到了
         */
    }

    /**
     * 使用lombok和JsonProperty的入参实体get/set方法测试接口
     * @param dto 入参数据
     */
    @PostMapping("useLombokAndProperty")
    public void useLombokAndPropertyTest(@RequestBody UseLombokAndPropertyDto dto){
        System.out.println("Use lombok and JsonProperty dto: ");
        if(dto == null) {
            System.out.println("dto: null");
        } else {
            System.out.println("aInt: " + dto.getAInt());
            System.out.println("oneInt: " + dto.getOneInt());
            System.out.println("aStr: " + dto.getAStr());
            System.out.println("oneStr: " + dto.getOneStr());
        }
        /**
         * 结果示例：
         * Use lombok and JsonProperty dto:
         * aInt: 0
         * oneInt: 0
         * aStr: abc
         * oneStr: abc
         */
    }
}
