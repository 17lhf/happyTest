package com.basic.happytest.modules.mp.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.happytest.modules.mp.entity.Mp;
import com.basic.happytest.modules.mp.service.MpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * MyBatisPlus相关实验的控制层
 * @author lhf
 */

@RestController
@CrossOrigin
@RequestMapping("/mp")
public class MpController {

    @Autowired
    private MpService mpService;

    /**
     * 分页查询
     * @param params 查询参数
     * @return 分页结果
     */
    @GetMapping("/query")
    public Page<Mp> query(@RequestParam Map<String, Object> params) {
        Page<Mp> page = mpService.selectPageVo(params);
        System.out.println(JSON.toJSONString(page));
        return page;
    }

}
