package com.basic.happytest.modules.lombokExplore.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 使用lombok的注解的实体
 * @author lhf
 */

@Getter
@Setter
public class UseLombokDto {

    /**
     * 首字母小写，第二个字母大写的变量
     */
    private Integer aInt;

    private Integer oneInt;

    /**
     * 首字母小写，第二个字母大写的变量
     */
    private String aStr;

    private String oneStr;
}
