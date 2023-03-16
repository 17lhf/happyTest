package com.basic.happytest.modules.cryptology.enums;

/**
 * 密钥长度枚举
 * @author lhf
 */

public enum KeyLengthEnums {

    DH_1024(1024),

    DSA_1024(1024),

    RSA_1024(1024),

    RSA_2048(2048),

    EC_256(256),

    ;

    private final int length;

    KeyLengthEnums(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
