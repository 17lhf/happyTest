package com.basic.happytest.modules.cryptology.enums;

/**
 * 证书类型枚举
 * @author lhf
 */

public enum CertTypeEnum {

    X509("X.509");

    private final String type;

    CertTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
