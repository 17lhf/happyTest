package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.KeyAlgorithmEnum;
import com.basic.happytest.modules.cryptology.enums.KeyLengthEnums;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysmmetricUtilsTest {

    @Test
    void generateKey() throws Exception {
        SysmmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_128.getLength());
        SysmmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_192.getLength());
        SysmmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_256.getLength());
        SysmmetricUtils.generateKey(KeyAlgorithmEnum.DES.getAlgorithm(), KeyLengthEnums.DES_56.getLength());
        SysmmetricUtils.generateKey(KeyAlgorithmEnum.TDES.getAlgorithm(), KeyLengthEnums.TDES_112.getLength());
        SysmmetricUtils.generateKey(KeyAlgorithmEnum.TDES.getAlgorithm(), KeyLengthEnums.TDES_168.getLength());
    }
}