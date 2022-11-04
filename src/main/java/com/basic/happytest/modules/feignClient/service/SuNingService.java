package com.basic.happytest.modules.feignClient.service;

import feign.Headers;
import feign.RequestLine;

/**
 * 获取苏宁服务器的系统事件的接口
 * @author lhf
 */

public interface SuNingService {

    /**
     * 获取系统时间
     * @return 接口响应结果
     */
    @RequestLine("GET /getSysTime.do")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Object getSysTime();
}
