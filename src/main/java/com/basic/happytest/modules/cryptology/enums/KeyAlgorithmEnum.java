package com.basic.happytest.modules.cryptology.enums;

/**
 * 密钥算法枚举
 * @author lhf
 */

public enum KeyAlgorithmEnum {

    RSA("RSA"),

    DH("DiffieHellman"),

    DSA("DSA"),

    EC("EC"),

    ECDSA("ECDSA"),

    ;


    private final String algorithm;

    KeyAlgorithmEnum(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
