package com.basic.happytest.modules.cryptology.enums;

/**
 * 摘要方式枚举
 * @author lhf
 */

public enum DigestTypeEnum {

    MD5("MD5"),

    SHA1("SHA1"),

    SHA_1("SHA-1"),

    SHA256("SHA256"),

    SHA_256("SHA-256"),

    ;

    private final String digestType;

    DigestTypeEnum(String digestType) {
        this.digestType = digestType;
    }

    public String getDigestType() {
        return digestType;
    }
}
