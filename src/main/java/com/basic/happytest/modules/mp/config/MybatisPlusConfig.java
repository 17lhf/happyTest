package com.basic.happytest.modules.mp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置
 * @author lhf
 */

@Configuration
public class MybatisPlusConfig {

    /**
     * 配置插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件(如果配置多个插件,切记分页最后添加)
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL); // dbType(数据库类型)
        paginationInnerInterceptor.setOverflow(true); // 默认值：false。溢出总页数后是否进行处理。
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}