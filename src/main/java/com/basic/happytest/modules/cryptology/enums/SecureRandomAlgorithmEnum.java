package com.basic.happytest.modules.cryptology.enums;

/**
 * 安全随机数生成器算法支持枚举，依赖于算法提供者支持的算法
 * @author : lhf
 */

public enum SecureRandomAlgorithmEnum {

    /**
     * BC库支持
     */
    DEFAULT("DEFAULT"),

    /**
     * BC库支持
     */
    NONCEANDIV("NONCEANDIV"),

    /**
     * SUN库支持
     */
    SHA1PRNG("SHA1PRNG");

    private final String algo;

    SecureRandomAlgorithmEnum(String algo) {
        this.algo = algo;
    }

    public String getAlgo() {
        return algo;
    }
}
