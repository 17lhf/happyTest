package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.KeyAlgorithmEnum;
import com.basic.happytest.modules.cryptology.enums.KeyLengthEnums;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class ASN1UtilsTest {

    @Test
    void printParseASN1RsaPubKey() throws Exception {
        // 2048: 30 8 2 0122 30 0d 06 09 2a864886f70d010101 0500 03 8 2 010f 00
        // 1024: 30 8 1 9f   30 0d 06 09 2a864886f70d010101 0500 03 8 1 8d 00
        // 获得PKCS8标准密钥对
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(),
                KeyLengthEnums.RSA_2048.getLength());
        PublicKey oriPubKey = keyPair.getPublic();
        byte[] p8PubKeyBytes = oriPubKey.getEncoded();
        // 获得PKCS1标准公钥
        byte[] p1PubKeyBytes = AsymmetricUtils.p8PubKey2P1PubKeyBytes(keyPair.getPublic());
        System.out.println(Hex.toHexString(p8PubKeyBytes));
        System.out.println(Hex.toHexString(p1PubKeyBytes));
        // 输出信息
        ASN1Utils.printParseASN1RsaPubKey(Hex.toHexString(p8PubKeyBytes), true);
        System.out.println();
        ASN1Utils.printParseASN1RsaPubKey(Hex.toHexString(p1PubKeyBytes), false);
    }
}