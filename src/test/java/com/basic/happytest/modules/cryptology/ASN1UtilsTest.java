package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.KeyAlgorithmEnum;
import com.basic.happytest.modules.cryptology.enums.KeyLengthEnums;
import org.bouncycastle.util.encoders.Hex;
import org.ietf.jgss.GSSException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    void oid2DERString() throws GSSException, IOException {
        List<String> oidList = new ArrayList<>();
        oidList.add("1.3.6.1.5.5.7.3.8");
        oidList.add("1.3.6.1.5.5.7.3.8");
        // 3014 06082b06010505070308 06082b06010505070308
        System.out.println(ASN1Utils.oid2DERString(oidList));
    }
}