package com.basic.happytest.modules.cryptology.enums;

/**
 * 算法提供者枚举
 * @author lhf
 */

public enum ProviderEnums {

    SUN("SunJCE"),

    BC("BC");

    private final String provider;

    ProviderEnums(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}
