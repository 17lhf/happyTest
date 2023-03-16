package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.DigestTypeEnum;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

class DigestUtilsTest {

    @Test
    void getDigestValue() throws NoSuchAlgorithmException {
        DigestUtils.digestData("Message".getBytes(StandardCharsets.UTF_8), DigestTypeEnum.MD5.getDigestType());
        DigestUtils.digestData("Message".getBytes(StandardCharsets.UTF_8), DigestTypeEnum.SHA1.getDigestType());
        DigestUtils.digestData("Message".getBytes(StandardCharsets.UTF_8), DigestTypeEnum.SHA_1.getDigestType());
        // 不支持SHA256, 只支持SHA-256
        // DigestUtils.digestData("Message".getBytes(StandardCharsets.UTF_8), DigestTypeEnum.SHA256.getDigestType());
        DigestUtils.digestData("Message".getBytes(StandardCharsets.UTF_8), DigestTypeEnum.SHA_256.getDigestType());
    }
}