package com.basic.happytest.modules.jaCoCo;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class JaCoCoInstanceT {

    @Test
    void max() {
        Assert.isNull(JaCoCoInstance.max(null, 1));
        Assert.isNull(JaCoCoInstance.max(1, null));
        Assert.isTrue(JaCoCoInstance.max(null, null) == null);
        Assert.isTrue(JaCoCoInstance.max(1, 2) == 2);
        Assert.isTrue(JaCoCoInstance.max(2, 1) == 2);
    }
}