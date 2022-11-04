package com.basic.happytest.modules.feignClient.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用来传递格式数据的对象
 * @author lhf
 */

@Getter
@Setter
public class FormatDto {
    /**
     * 格式
     */
    private String format;

    public FormatDto(String format) {
        this.format = format;
    }
}
