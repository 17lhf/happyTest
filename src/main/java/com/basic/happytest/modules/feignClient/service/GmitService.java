package com.basic.happytest.modules.feignClient.service;

import com.basic.happytest.modules.feignClient.entity.FormatDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 官网地址：https://api.gmit.vip/Docs/#/ 一个用来免费测试接口的网站
 * @author lhf
 */

public interface GmitService {

    /**
     * 获取随机一言
     * @param format 返回数据格式
     * @return 响应的数据
     */
    @RequestLine("GET /Api/YiYan")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Object getYiYan(@Param("format")String format);

    /**
     * 获取随机文案
     * @param formatDto 返回数据格式
     * @return 响应的数据
     */
    @RequestLine("POST /Api/WaSentence")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Object getWaSentence(FormatDto formatDto);
}
