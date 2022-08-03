package com.basic.happytest.modules.lombokExplore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 使用lombok的注解,并使用JsonProperty注解解决其不符合规范问题的实体
 * @author lhf
 */
@Getter
@Setter
public class UseLombokAndPropertyDto {
    /**
     * 首字母小写，第二个字母大写的变量 <br/>
     * 使用JsonProperty注解把该属性的名称序列化为aInt
     */
    @JsonProperty("aInt")
    private Integer aInt;

    private Integer oneInt;

    /**
     * 首字母小写，第二个字母大写的变量 <br/>
     * 使用JsonProperty注解把该属性的名称序列化为aStr
     */
    @JsonProperty("aStr")
    private String aStr;

    private String oneStr;
}
