package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.fileIO.FileIO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;


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
    // RSA CERT示例
    static String RSA_CERT_PEM = "static/cryptologyFiles/rsa1.crt";
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
        Cryptology.digestData("Message".getBytes(StandardCharsets.UTF_8), "MD5");
        Cryptology.digestData("Message".getBytes(StandardCharsets.UTF_8), "SHA-1");
        Cryptology.digestData("Message".getBytes(StandardCharsets.UTF_8), "SHA-256");
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
        Cryptology.generateP10CertRequest("RSA", 2048, csrInfos);
        Cryptology.generateP10CertRequest("EC", 256, csrInfos);
    }

    @Test
    void generateECCKeyPair() throws Exception {
        Cryptology.generateECCKeyPair(256);
    }

    @Test
    void loadRSAPrivateKey() throws Exception {
        Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
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
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest("RSA", 2048, csrInfos);
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
        Cryptology.getPubKeyFromCsr(Cryptology.generateP10CertRequest("EC", 256, csrInfos), "EC");
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
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest("RSA",2048, csrInfos);
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


        String ecAlo = "ECIES";
        int ecSize = 256;
        KeyPair ecKeyPair = Cryptology.generateECCKeyPair(ecSize);
        byte[] ecEncData = Cryptology.encryptData(ecKeyPair.getPublic(), ecAlo, sBytes);
        byte[] ecDecData = Cryptology.decryptData(ecKeyPair.getPrivate(), ecAlo, ecEncData);
        System.out.println(new String(ecDecData, StandardCharsets.UTF_8));

        String alo = "RSA";
        Integer size = 2048;
        KeyPair keyPair = Cryptology.generateKeyPair(alo, size);
        byte[] encData1 = Cryptology.encryptData(keyPair.getPrivate(), alo, sBytes);
        byte[] encData2 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/PKCS1Padding", sBytes);
        byte[] encData3 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/OAEPWithSHA-1AndMGF1Padding", sBytes);
        byte[] encData4 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/OAEPWithSHA-256AndMGF1Padding", sBytes);
        byte[] encData5 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/NoPadding", sBytes);
        byte[] decData1 = Cryptology.decryptData(keyPair.getPublic(), alo, encData1);
        byte[] decData2 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/PKCS1Padding", encData2);
        byte[] decData3 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/OAEPWithSHA-1AndMGF1Padding", encData3);
        byte[] decData4 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/OAEPWithSHA-256AndMGF1Padding", encData4);
        byte[] decData5 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/NoPadding", encData5);
        System.out.println(new String(decData1, StandardCharsets.UTF_8));
        System.out.println(new String(decData2, StandardCharsets.UTF_8));
        System.out.println(new String(decData3, StandardCharsets.UTF_8));
        System.out.println(new String(decData4, StandardCharsets.UTF_8));
        System.out.println(new String(decData5, StandardCharsets.UTF_8));
    }

    @Test
    void keyAloSupportedInBCLibrary() {
        Cryptology.keyAloSupportedInBCLibrary();
    }

    @Test
    void certPem2DerHexStr() throws Exception {
        Cryptology.certPem2DerHexStr(FileIO.getFileContent(FileIO.getAbsolutePath(CA_CERT_PEM)));
    }

    @Test
    void generateP12() throws Exception {
        X509Certificate rootCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM");
        X509Certificate subCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), "PEM");
        PrivateKey privateKey = Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        Cryptology.generateP12(rootCert, "rootCert",
                subCert, "sub",
                privateKey, "654321", "123456", FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void signData() throws Exception {
        String data = "a data to be signed";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        // RSA
        PrivateKey privateKey = Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        System.out.println();
        byte[] res1 = Cryptology.signData(privateKey, "RSA", dataBytes, "SHA256");
        System.out.println();
        byte[] res2 = Cryptology.signData2(privateKey, "RSA", dataBytes, "SHA256");
        System.out.println(Hex.toHexString(res1).equals(Hex.toHexString(res2)));
        PublicKey publicKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        System.out.println();
        /*Cryptology.decryptData(publicKey, "RSA", res1);
        System.out.println();
        Cryptology.decryptData(publicKey, "RSA", res2);
        System.out.println();*/
        Cryptology.decryptData(publicKey, "RSA/ECB/PKCS1Padding", res1);
        System.out.println();
        Cryptology.decryptData(publicKey, "RSA/ECB/PKCS1Padding", res2);

        // ECC
        PrivateKey ecPrvKey = Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
        System.out.println();
        Cryptology.signData(ecPrvKey, "ECDSA", dataBytes, "SHA256");
    }

    @Test
    void validSignature() throws Exception {
        String data = "a data to be signed";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        // RSA
        PrivateKey privateKey = Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        System.out.println();
        byte[] res1 = Cryptology.signData(privateKey, "RSA", dataBytes, "SHA256");
        PublicKey publicKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        System.out.println();
        Cryptology.validSignature(publicKey, "RSA", dataBytes, "SHA256", res1);
        // ECC
        PrivateKey ecPrvKey = Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
        System.out.println();
        byte[] res11 = Cryptology.signData(ecPrvKey, "ECDSA", dataBytes, "SHA256");
        PublicKey ecPubKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(ECC_PUB_KEY));
        Cryptology.validSignature(ecPubKey, "ECDSA", dataBytes, "SHA256", res11);
    }

    @Test
    void validCertChain() throws Exception {
        X509Certificate subCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), "PEM");
        X509Certificate rootCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM");
        Cryptology.validCertChain(subCert, rootCert);
        Cryptology.validCertChain(rootCert, subCert);
    }

    @Test
    void loadAndParseP12() throws Exception {
       Cryptology.loadAndParseP12(FileIO.getAbsolutePath(STORE_PATH) + "/genByJavaRSA1.p12",
               "123456", "sub", "654321", "sub", "rootCert");
    }

    @Test
    public void genP10() throws Exception{
        PublicKey publicKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        PrivateKey privateKey = Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("genP10");
        csrInfos.setEmailAddress("lhf@qq.com");
        byte[] csrInfoBytes = Cryptology.getP10CsrInfoToBeSign(csrInfos, publicKey);
        byte[] signature = Cryptology.signData(privateKey, "RSA", csrInfoBytes, "SHA256");
        PKCS10CertificationRequest csr = Cryptology.constructP10Csr(signature, csrInfoBytes);
        Cryptology.csr2PemFile(csr, FileIO.getAbsolutePath(STORE_PATH) + "/");
    }
}