package com.basic.happytest.modules.cryptology.enums;

/**
 * pem格式的数据的类型
 * @author lhf
 */

public enum PemTypeEnum {

    PRV_KEY("PRIVATE KEY"),

    PUB_KEY("PUBLIC KEY"),

    CSR("CERTIFICATE REQUEST"),

    CERT("CERTIFICATE");

    private final String type;

    PemTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
