package com.basic.happytest.modules.property.customProperty;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 自定义配置控制层
 * @author lhf
 */

@RestController
@CrossOrigin
@RequestMapping("/customConfig")
public class CustomConfigController {

    /**
     * 更新配置
     * @param params 新配置的数据
     * @return 成功的结果
     */
    @PostMapping("/updConfig")
    public String updConfig(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String age = params.get("age");
        System.out.println("收到的name: " + name);
        System.out.println("收到的age: " + age);
        CustomConfig.updProps(name, age);
        return "Success";
    }
}
