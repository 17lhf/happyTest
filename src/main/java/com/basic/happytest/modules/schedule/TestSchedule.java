package com.basic.happytest.modules.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于注解的定时任务相关示例 <br />
 * 说明：<br />
 * 基于注解@Scheduled默认为单线程，同一时间开启多个任务时，任务的执行时机会受上一个任务执行时间的影响<br />
 * 可以在配置文件或SchedulingConfigurer中去修改定时任务的默认配置
 * @author lhf
 */

@Component
public class TestSchedule {

    private final Logger logger = LoggerFactory.getLogger(TestSchedule.class);

    /**
     * 通过加载配置文件中的配置来设置定时任务的运行时间
     */
    @Scheduled(cron = "${schedule.cron1}")
    public void doTask() {
        logger.info("Done task.");
    }
}
