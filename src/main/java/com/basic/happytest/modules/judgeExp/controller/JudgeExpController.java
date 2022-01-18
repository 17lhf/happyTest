package com.basic.happytest.modules.judgeExp.controller;

import com.basic.happytest.modules.judgeExp.entity.JudgeExp;
import com.basic.happytest.modules.judgeExp.service.JudgeExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 测试从前端传来数据时，接收的数据拿去作为查询条件，会怎样
 * @author lhf
 */

@RestController
@RequestMapping("/judge-exp/")
@CrossOrigin
public class JudgeExpController {

    @Autowired
    JudgeExpService judgeExpService;

    /**
     * 实验从外部传参过来时，把map中的“0”值拿去xml中的if语句判断，看看会怎样
     * @param params 查询参数
     * @return 符合条件的列表
     *
     * 示例params:
     * numValue: 0
     * 注意路径是：localhost:12312/happyTest/judge-exp/select
     * 注意：Content-Type：application/json;charset=UTF-8
     */
    @GetMapping("select")
    public List<JudgeExp> select(@RequestParam Map<String, Object> params){
        return judgeExpService.select(params);
        // 结果是不会把0认为是空字符串，一切正常
    }
}
