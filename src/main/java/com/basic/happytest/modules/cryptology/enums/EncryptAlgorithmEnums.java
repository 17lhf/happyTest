package com.basic.happytest.modules.cryptology.enums;

/**
 * 加密算法枚举
 * 实际支持的算法类型至少包含这些（可参考）：https://www.runoob.com/manual/jdk11api/java.base/javax/crypto/Cipher.html
 * @author lhf
 */

public enum EncryptAlgorithmEnums {
    /**
     * SunJCE下加密结果等同于“DES/ECB/PKCS5Padding”
     */
    DES("DES"),

    /**
     * 此时要求数据长度为 8*n 字节 <br />
     * CBC模式需要初始化向量(见使用示例)
     */
    DES_CBC_NOPADDING("DES/CBC/NoPadding"),

    /**
     * CBC模式需要初始化向量(见使用示例)
     */
    DES_CBC_PKCS5PADDING("DES/CBC/PKCS5Padding"),

    DES_ECB_PKCS5PADDING("DES/ECB/PKCS5Padding"),

    /**
     * SunJCE下加密结果等同于“DESede/ECB/PKCS5Padding”
     */
    DESEDE("DESede"),

    /**
     * 此时要求数据长度为 8*n 字节 <br />
     * CBC模式需要初始化向量(见使用示例)
     */
    DESEDE_CBC_NOPADDING("DESede/CBC/NoPadding"),

    /**
     * CBC模式需要初始化向量(见使用示例)
     */
    DESEDE_CBC_PKCS5PADDING("DESede/CBC/PKCS5Padding"),

    DESEDE_ECB_PKCS5PADDING("DESede/ECB/PKCS5Padding"),

    /**
     * SunJCE下加密结果等同于“AES/CBC/PKCS5Padding”，但是不用初始化向量
     */
    AES("AES"),

    /**
     * 此时要求数据长度为 16*n 字节 <br />
     * CBC模式需要初始化向量(见使用示例)
     */
    AES_CBC_NOPADDING("AES/CBC/NoPadding"),

    /**
     * CBC模式需要初始化向量（见使用示例）
     */
    AES_CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),

    /**
     * 此时要求数据长度为 16*n 字节
     */
    AES_ECB_NOPADDING("AES/ECB/NoPadding"),

    /**
     * BC库的话等同于“RSA/ECB/NoPadding”
     */
    RSA("RSA"),

    RSA_ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding"),

    RSA_NONE_OAEP_WITH_SHA1_AND_MGF1PADDING("RSA/None/OAEPWithSHA-1AndMGF1Padding"),

    // OAEP cannot be used to sign or verify signatures
    RSA_ECB_OAEP_WITH_SHA1_AND_MGF1PADDING("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),

    RSA_NONE_OAEP_WITH_SHA256_AND_MGF1PADDING("RSA/None/OAEPWithSHA-256AndMGF1Padding"),

    // OAEP cannot be used to sign or verify signatures
    RSA_ECB_OAEP_WITH_SHA256_AND_MGF1PADDING("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),

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
