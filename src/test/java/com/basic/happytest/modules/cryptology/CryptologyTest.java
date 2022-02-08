package com.basic.happytest.modules.cryptology;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


/**
 * 密码学相关内容的测试
 * @author lhf
 */

class CryptologyTest {
    // <editor-fold desc="一些会被用到的文件路径">
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

    /**
     * 依据相对路径获取绝对路径
     * @param path resource文件夹底下的相对路径
     * @return 绝对路径
     * @throws IOException 异常
     */
    String getAbsolutePath(String path) throws IOException {
        String pemPath = (new ClassPathResource(path)).getFile().getPath();
        System.out.println("文件路径为： " + pemPath);
        return pemPath;
    }

    @Test
    void generateKeyPair() throws Exception {
        Cryptology.generateKeyPair("RSA");
    }

    @Test
    void getPubKeyFromCert() throws Exception {
       Cryptology.getPubKeyFromCert(getAbsolutePath(CA_CERT_PEM));
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
        Cryptology.loadRSAPrivateKey(getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
    }

    @Test
    void issueCert() throws Exception {
        String issuerCertPath = getAbsolutePath(CA_CERT_PEM);
        String issuerKeyPath = getAbsolutePath(CA_KEY);
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
        Cryptology.loadPKCS8EncryptedPrivateKey(getAbsolutePath(RSA_PRV_KEY_PKCS8_ENCRYPT), RSA_PRV_KEY_PKCS8_ENCRYPT_PWD);
        Cryptology.loadPKCS8EncryptedPrivateKey(getAbsolutePath(ECC_PRV_KEY_PKCS8_ENCRYPT), ECC_PRV_KEY_PKCS8_ENCRYPT_PWD);
    }

    @Test
    void loadPrivateKey() throws Exception {
        Cryptology.loadPKCS8PrivateKey(getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        Cryptology.loadPKCS8PrivateKey(getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
    }

    @Test
    void loadPublicKey() throws Exception {
        Cryptology.loadPublicKey(getAbsolutePath(RSA_PUB_KEY));
        Cryptology.loadPublicKey(getAbsolutePath(ECC_PUB_KEY));
    }

    @Test
    void loadPublicKey2() throws Exception {
        Cryptology.loadPublicKey2(getAbsolutePath(RSA_PUB_KEY), "RSA");
        Cryptology.loadPublicKey2(getAbsolutePath(ECC_PUB_KEY), "EC");
    }

    @Test
    void loadCertFromFile() throws Exception {
        Cryptology.loadCertFromFile(getAbsolutePath(CA_CERT_PEM), "PEM");
        Cryptology.loadCertFromFile(getAbsolutePath(CA_CERT_DER), "DER");
    }

    @Test
    void getCertMsg() throws Exception {
        Cryptology.getCertMsg(Cryptology.loadCertFromFile(getAbsolutePath(CA_CERT_PEM), "PEM"));
    }

    @Test
    void getPubKeyFromCsr() throws Exception {
        PKCS10CertificationRequest csr = Cryptology.loadCsrFromFile(getAbsolutePath(RSA_CSR_PEM), "PEM");
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
        Cryptology.loadCsrFromFile(getAbsolutePath(RSA_CSR_PEM), "PEM");
        Cryptology.loadCsrFromFile(getAbsolutePath(RSA_CSR_DER), "DER");
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
}