package com.basic.happytest.modules.feignClient;

import com.alibaba.fastjson.JSON;
import com.basic.happytest.modules.feignClient.entity.FormatDto;
import com.basic.happytest.modules.feignClient.service.GmitService;
import com.basic.happytest.modules.feignClient.service.SuNingService;
import org.junit.jupiter.api.Test;

class FeignClientTest {

    @Test
    void getSuNingSysTime() {
        String suNingRootUri = "http://quan.suning.com";
        FeignClient feignClient = new FeignClient(suNingRootUri);
        Object suNingResult = feignClient.getService(SuNingService.class).getSysTime();
        if(suNingResult != null) {
            System.out.println("获取苏宁服务器的系统时间应答：" + JSON.toJSONString(suNingResult));
        }
    }

    @Test
    void getYiYan() {
        String gmitRootUri = "https://api.gmit.vip";
        FeignClient feignClient = new FeignClient(gmitRootUri);
        Object result = feignClient.getService(GmitService.class).getYiYan("json");
        if(result != null) {
            System.out.println("应答：" + JSON.toJSONString(result));
        }
    }

    @Test
    void getWaSentence() {
        FormatDto formatDto = new FormatDto("json");
        String gmitRootUri = "https://api.gmit.vip";
        FeignClient feignClient = new FeignClient(gmitRootUri);
        Object result = feignClient.getService(GmitService.class).getWaSentence(formatDto);
        if(result != null) {
            System.out.println("应答：" + JSON.toJSONString(result));
        }
    }
}