package com.basic.happytest.modules.schedule;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * todo 通过实现SchedulingConfigurer来配置定时任务的一些参数、实现加载数据库配置执行周期的动态定时任务
 * @author lhf
 */

public class TestScheduleConfigurer implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    }
}
