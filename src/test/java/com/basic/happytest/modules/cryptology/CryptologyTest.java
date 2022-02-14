package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.fileIO.FileIO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


/**
 * 密码学相关内容的测试
 * @author lhf
 */

class CryptologyTest {
    // <editor-fold desc="一些会被用到的文件路径">
    // 存储生成的文件的路径
    static String STORE_PATH = "static/cryptologyFiles/storeFiles/";
    // RSA私钥示例1
    static String RSA_PRV_KEY_PKCS1_NO_ENCRYPT = "static/cryptologyFiles/rsaPrivateKey1.key";
    static String RSA_PRV_KEY_PKCS8_NO_ENCRYPT = "static/cryptologyFiles/pkcs8RsaPrivateKey1.key";
    // RSA公钥示例1
    static String RSA_PUB_KEY = "static/cryptologyFiles/rsaPublicKey1.key";
    // RSA私钥示例2
    static String RSA_PRV_KEY_PKCS1_ENCRYPT = "static/cryptologyFiles/rsaProtectedByPwd.key";
    static String RSA_PRV_KEY_PKCS8_ENCRYPT = "static/cryptologyFiles/pkcs8RsaProtectedByPwd.key";
    static String RSA_PRV_KEY_PKCS8_ENCRYPT_PWD = "123456";
    // RSA CSR示例
    static String RSA_CSR_PEM = "static/cryptologyFiles/rsa1.csr";
    static String RSA_CSR_DER = "static/cryptologyFiles/rsa1_der.csr";
    // ECC私钥示例1
    static String ECC_PRV_KEY_PKCS1_NO_ENCRYPT = "static/cryptologyFiles/ecPrivateKey1.key";
    static String ECC_PRV_KEY_PKCS1_ENCRYPT = "static/cryptologyFiles/ecProtectedPrvKey1.key";
    static String ECC_PRV_KEY_PKCS1_ENCRYPT_PWD = "123456";
    static String ECC_PRV_KEY_PKCS8_ENCRYPT = "static/cryptologyFiles/ecPkcs8ProtectedPrivateKey1.key";
    static String ECC_PRV_KEY_PKCS8_ENCRYPT_PWD = "123456";
    static String ECC_PRV_KEY_PKCS8_NO_ENCRYPT = "static/cryptologyFiles/ecPkcs8PrivateKey1.key";
    // ECC公钥示例1
    static String ECC_PUB_KEY = "static/cryptologyFiles/ecPublicKey1.key";
    // CA证书示例
    static String CA_CERT_PEM = "static/cryptologyFiles/ca.crt";
    static String CA_CERT_DER = "static/cryptologyFiles/ca.der";
    // CA私钥示例
    static String CA_KEY = "static/cryptologyFiles/ca.key";
    // </editor-fold>

    @Test
    void testGetAbsoluteFolderPath() throws IOException {
        FileIO.getAbsolutePath(STORE_PATH);
    }

    @Test
    void generateKeyPair() throws Exception {
        Cryptology.generateKeyPair("RSA", 2048);
        Cryptology.generateKeyPair("DH", 1024);
        Cryptology.generateKeyPair("DSA", 1024);
    }

    @Test
    void getPubKeyFromCert() throws Exception {
       Cryptology.getPubKeyFromCert(FileIO.getAbsolutePath(CA_CERT_PEM));
    }

    @Test
    void getDigestValue() throws NoSuchAlgorithmException {
        Cryptology.getDigestValue("Message", "MD5");
        Cryptology.getDigestValue("Message", "SHA-1");
        Cryptology.getDigestValue("Message", "SHA-256");
    }

    @Test
    void generateP10CertRequest() throws Exception {
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        Cryptology.generateP10CertRequest("RSA", csrInfos);
        Cryptology.generateP10CertRequest("EC", csrInfos);
    }

    @Test
    void generateECCKeyPair() throws Exception {
        Cryptology.generateECCKeyPair();
    }

    @Test
    void loadRSAPrivateKey() throws Exception {
        Cryptology.loadRSAPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
    }

    @Test
    void issueCert() throws Exception {
        String issuerCertPath = FileIO.getAbsolutePath(CA_CERT_PEM);
        String issuerKeyPath = FileIO.getAbsolutePath(CA_KEY);
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest("RSA", csrInfos);
        Cryptology.issueCert(csr, issuerCertPath, issuerKeyPath, 3650);
    }

    @Test
    void loadPKCS8EncryptedPrivateKey() throws Exception {
        Cryptology.loadPKCS8EncryptedPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_ENCRYPT), RSA_PRV_KEY_PKCS8_ENCRYPT_PWD);
        Cryptology.loadPKCS8EncryptedPrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_ENCRYPT), ECC_PRV_KEY_PKCS8_ENCRYPT_PWD);
    }

    @Test
    void loadPrivateKey() throws Exception {
        Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
    }

    @Test
    void loadPublicKey() throws Exception {
        Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        Cryptology.loadPublicKey(FileIO.getAbsolutePath(ECC_PUB_KEY));
    }

    @Test
    void loadPublicKey2() throws Exception {
        Cryptology.loadPublicKey2(FileIO.getAbsolutePath(RSA_PUB_KEY), "RSA");
        Cryptology.loadPublicKey2(FileIO.getAbsolutePath(ECC_PUB_KEY), "EC");
    }

    @Test
    void loadCertFromFile() throws Exception {
        Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM");
        Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_DER), "DER");
    }

    @Test
    void getCertMsg() throws Exception {
        Cryptology.getCertMsg(Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM"));
    }

    @Test
    void getPubKeyFromCsr() throws Exception {
        PKCS10CertificationRequest csr = Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), "PEM");
        Cryptology.getPubKeyFromCsr(csr, "RSA");

        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        Cryptology.getPubKeyFromCsr(Cryptology.generateP10CertRequest("EC", csrInfos), "EC");
    }

    @Test
    void testLoadCsrFromFile() throws Exception {
        Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), "PEM");
        Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_DER), "DER");
    }

    @Test
    void getCsrMsg() throws Exception {
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest("RSA", csrInfos);
        Cryptology.getCsrMsg(csr);
    }

    @Test
    void key2PemOutPut() throws Exception {
        Cryptology.key2PemOutPut("RSA", 2048);
        // Cryptology.key2PemOutPut("DSA", 1024);
        // Cryptology.key2PemOutPut("DH", 1024);
    }

    @Test
    void key2PemFile() throws Exception {
        Cryptology.key2PemFile("RSA", 2048, FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void der2pem() throws Exception {
        /*String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY)).getEncoded()), "PUB_KEY");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_PUB_KEY))));*/
        String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT)).getEncoded()), "PRV_KEY");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT))));
        /*String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), "PEM").getEncoded()), "CSR");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_CSR_PEM))));*/
        /*String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadCertFromFile(FileIO.getAbsolutePath(), "PEM").getEncoded()), "CERT");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath())));*/
    }

    @Test
    void cert2PemFile() throws Exception {
        Cryptology.cert2PemFile(Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM"),
                FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void encryptAndDecryptData() throws Exception {
        String s = "abc123,.中文";
        byte[] sBytes = s.getBytes(StandardCharsets.UTF_8);
        KeyPair keyPair = Cryptology.generateKeyPair("RSA", 2048);
        String encData1 = Cryptology.encryptData(keyPair.getPrivate(), "RSA", sBytes, 1);
        String encData2 = Cryptology.encryptData(keyPair.getPrivate(), "RSA", sBytes, 2);
        String decData1 = Cryptology.decryptData(keyPair.getPublic(), "RSA", Hex.decode(encData1), 1);
        String decData2 = Cryptology.decryptData(keyPair.getPublic(), "RSA", Base64.getDecoder().decode(encData2), 2);
        System.out.println(new String(Hex.decode(decData1), StandardCharsets.UTF_8));
        System.out.println(new String(Base64.getDecoder().decode(decData2), StandardCharsets.UTF_8));
    }
}