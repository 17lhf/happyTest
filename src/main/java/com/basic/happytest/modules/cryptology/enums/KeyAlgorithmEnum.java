package com.basic.happytest.modules.cryptology.enums;

/**
 * 密钥算法枚举
 * @author lhf
 */

public enum KeyAlgorithmEnum {
    // 非对称密钥
    RSA("RSA"),
    DH("DiffieHellman"),
    DSA("DSA"),
    EC("EC"),
    ECDSA("ECDSA"),

    // 对称密钥
    AES("AES"),
    DES("DES"),
    TDES("DESede")
    ;


    private final String algorithm;

    KeyAlgorithmEnum(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
