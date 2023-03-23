package com.basic.happytest.modules.cryptology.enums;

/**
 * 加密算法枚举
 * @author lhf
 */

public enum EncryptAlgorithmEnums {
    RSA("RSA"),

    RSA_ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding"),

    RSA_NONE_OAEP_WITH_SHA1_AND_MGF1PADDING("RSA/None/OAEPWithSHA-1AndMGF1Padding"),

    RSA_NONE_OAEP_WITH_SHA256_AND_MGF1PADDING("RSA/None/OAEPWithSHA-256AndMGF1Padding"),

    RSA_ECB_NOPADDING("RSA/ECB/NoPadding"),

    RSA_NONE_NOPADDING("RSA/None/NoPadding"),

    RSA_NONE_PKCS1PADDING("RSA/None/PKCS1Padding"),

    ECIES("ECIES");

    private final String algorithm;

    EncryptAlgorithmEnums(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}