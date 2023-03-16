package com.basic.happytest.modules.cryptology.enums;

/**
 * KeyStore的类型
 * @author lhf
 */

public enum KeyStoreTypeEnum {

    JKS("JKS"),

    PKCS12("PKCS12");

    private final String type;

    KeyStoreTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
