package com.basic.happytest.modules.cryptology.enums;

/**
 * 签名算法枚举
 * @author lhf
 */

public enum SignAlgorithmEnum {

    SHA256_WITH_RSA("SHA256withRSA"),

    SHA256_WITH_ECDSA("SHA256withECDSA"),
    ;

    private String algorithm;

    SignAlgorithmEnum(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
