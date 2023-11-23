package com.basic.happytest.modules.property.customProperty;

/**
 * 自定义配置的枚举值
 * @author lhf
 */

public enum CustomConfigEnum {

    NAME("name"),

    AGE("age"),
    ;

    private final String keyName;

    CustomConfigEnum(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
