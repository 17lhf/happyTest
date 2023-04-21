package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.KeyAlgorithmEnum;
import com.basic.happytest.modules.cryptology.enums.KeyLengthEnums;
import com.basic.happytest.modules.cryptology.enums.ProviderEnums;
import org.junit.jupiter.api.Test;

import java.security.Key;

class SymmetricUtilsTest {

    @Test
    void generateKey() throws Exception {
        SymmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_128.getLength());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_192.getLength());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_256.getLength());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.DES.getAlgorithm(), KeyLengthEnums.DES_56.getLength());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.TDES.getAlgorithm(), KeyLengthEnums.TDES_112.getLength());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.TDES.getAlgorithm(), KeyLengthEnums.TDES_168.getLength());
    }

    @Test
    void generateKeyBC() throws Exception {
        SymmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_128.getLength(), ProviderEnums.BC.getProvider());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_192.getLength(), ProviderEnums.BC.getProvider());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.AES.getAlgorithm(), KeyLengthEnums.AES_256.getLength(), ProviderEnums.BC.getProvider());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.DES.getAlgorithm(), KeyLengthEnums.DES_56.getLength(), ProviderEnums.BC.getProvider());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.TDES.getAlgorithm(), KeyLengthEnums.TDES_112.getLength(), ProviderEnums.BC.getProvider());
        SymmetricUtils.generateKey(KeyAlgorithmEnum.TDES.getAlgorithm(), KeyLengthEnums.TDES_168.getLength(), ProviderEnums.BC.getProvider());
    }

    @Test
    void get2DESStandardKey() throws Exception {
        Key key = SymmetricUtils.generateKey(KeyAlgorithmEnum.TDES.getAlgorithm(), KeyLengthEnums.TDES_112.getLength());
        String standardKey = SymmetricUtils.get2DESStandardKey(key);
        System.out.println(standardKey);
    }
}