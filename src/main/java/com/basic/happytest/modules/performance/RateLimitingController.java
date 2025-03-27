package com.basic.happytest.modules.performance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 限流测试用的接口
 * @author : lhf
 */

@RestController
@RequestMapping("/rate-limiting")
public class RateLimitingController {

    @GetMapping("/wait-one-second")
    public String waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/wait-two-second")
    public String waitTwoSecond() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/immediately")
    public String immediately() {
        return "success";
    }
}
