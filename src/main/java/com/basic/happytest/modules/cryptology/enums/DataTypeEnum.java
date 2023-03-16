package com.basic.happytest.modules.cryptology.enums;

/**
 * 数据类型
 * @author lhf
 */

public enum DataTypeEnum {

    PUB_EY("PUB_KEY"),

    PRV_KEY("PRV_KEY"),

    CSR("CSR"),

    CERT("CERT");

    private final String type;

    DataTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
