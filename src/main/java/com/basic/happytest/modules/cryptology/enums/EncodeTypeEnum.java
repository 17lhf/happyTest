package com.basic.happytest.modules.cryptology.enums;

/**
 * 编码算法枚举
 * @author lhf
 */

public enum EncodeTypeEnum {

    PEM("PEM"),

    DER("DER");

    private String type;

    EncodeTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
