package com.basic.happytest.modules.cryptology;

import org.junit.jupiter.api.Test;

class CommonUtilsTest {

    @Test
    void keyAloSupportedInBCLibrary() {
        CommonUtils.keyAloSupportedInBCLibrary();
    }

    @Test
    void showProviders() {
        CommonUtils.showProviders();
    }

    @Test
    void keyAloSupportedInSunLibrary() {
        CommonUtils.keyAloSupportedInSunLibrary();
    }
}