package com.basic.happytest.modules.uniformPackagingReturn.controller;

import com.basic.happytest.modules.uniformPackagingReturn.model.TestVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一封装返回的控制层
 * @author lhf
 */

@RestController
@RequestMapping("/uniformPackagingReturn/")
public class UniformPackagingReturnController {

    /**
     * 应答数字
     * @return 数字数据
     * 应答结果：
     * {
     *     "code": 0,
     *     "msg": "OK",
     *     "data": 1
     * }
     */
    @GetMapping("num")
    public Integer getNumValue(){
        return 1;
    }

    /**
     * 应答字符串<br/>
     * 应答结果（抛异常）：<br/>
     * com.basic.happytest.modules.uniformPackagingReturn.resp.Result cannot be cast to java.lang.String<br/>
     * 原因参考：https://www.cnblogs.com/oldboyooxx/p/10824531.html<br/>
     * 其实就是因为StringHttpMessageConverter的问题<br/>
     * 在Resp中已经使用了一种简单的方案来解决这个问题<br/>
     * @return 字符串数据
     */
    @GetMapping("string")
    public String getStringValue(){
        return "abc";
    }

    /**
     * 应答对象
     * @return 对象数据
     */
    @GetMapping("object")
    public TestVO getObjectValue(){
        TestVO vo = new TestVO();
        vo.setNum(0);
        vo.setStr("abc");
        return vo;
        /**
         * 应答结果：
         * {
         *     "code": 0,
         *     "msg": "OK",
         *     "data": {
         *         "num": 0,
         *         "str": "abc"
         *     }
         * }
         */
    }
}
