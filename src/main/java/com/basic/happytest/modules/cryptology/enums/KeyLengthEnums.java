package com.basic.happytest.modules.cryptology.enums;

/**
 * 密钥长度枚举
 * @author lhf
 */

public enum KeyLengthEnums {
    // 非对称密钥
    DH_1024(1024),
    DSA_1024(1024),
    RSA_1024(1024),
    RSA_2048(2048),
    EC_256(256),

    // 对称密钥
    AES_128(128),
    AES_192(192),
    AES_256(256),
    DES_56(56), // 单倍长密钥
    TDES_112(112), // 双倍长
    TDES_168(168), // 三倍长
    ;

    private final int length;

    KeyLengthEnums(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
