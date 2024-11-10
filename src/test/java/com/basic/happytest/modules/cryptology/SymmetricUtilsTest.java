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

    @Test
    void hex2Key() throws Exception {
        String alo1 = KeyAlgorithmEnum.AES.getAlgorithm();
        Key key1 = SymmetricUtils.generateKey(alo1, KeyLengthEnums.AES_128.getLength());
        SymmetricUtils.hex2Key(SymmetricUtils.key2Hex(key1, alo1), alo1);
        Key key2 = SymmetricUtils.generateKey(alo1, KeyLengthEnums.AES_192.getLength());
        SymmetricUtils.hex2Key(SymmetricUtils.key2Hex(key2, alo1), alo1);
        Key key3 = SymmetricUtils.generateKey(alo1, KeyLengthEnums.AES_256.getLength());
        SymmetricUtils.hex2Key(SymmetricUtils.key2Hex(key3, alo1), alo1);

        String alo2 = KeyAlgorithmEnum.DES.getAlgorithm();
        Key key4 = SymmetricUtils.generateKey(alo2, KeyLengthEnums.DES_56.getLength());
        SymmetricUtils.hex2Key(SymmetricUtils.key2Hex(key4, alo2), alo2);

        String alo3 = KeyAlgorithmEnum.TDES.getAlgorithm();
        Key key5 = SymmetricUtils.generateKey(alo3, KeyLengthEnums.TDES_112.getLength());
        SymmetricUtils.hex2Key(SymmetricUtils.key2Hex(key5, alo3), alo3);
        Key key6 = SymmetricUtils.generateKey(alo3, KeyLengthEnums.TDES_168.getLength());
        SymmetricUtils.hex2Key(SymmetricUtils.key2Hex(key6, alo3), alo3);
    }
}