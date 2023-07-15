package com.basic.happytest.modules.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务相关配置
 * @author lhf
 */

@Configuration
@EnableScheduling // 这个注解也可以直接放在启动类上，用于开启定时任务管理，不配置的话就不会处理定时任务
// @EnableAsync // todo 开启支持定时任务的多线程执行支持
public class ScheduleConfig {
}
