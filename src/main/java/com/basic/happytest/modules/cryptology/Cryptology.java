package com.basic.happytest.modules.cryptology;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.*;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.x509.X509CertImpl;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.Date;

/**
 * 密码学相关的java操作
 * (注意部分内容会使用到BC库)
 * （注意，很多地方异常的处理没有做，请自行解决）
 * (注意，有些类在java安全库中有，在BC库中也有，要注意实际用的是哪一个)
 * @author lhf
 */

public class Cryptology {

    // 解决报错：no such provider: BC
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取摘要
     * @param msg 等待获取摘要的内容
     * @param alo 选择的算法， 可选：MD5、SHA-1、SHA-256
     * @throws NoSuchAlgorithmException 异常
     */
    public static void getDigestValue(String msg, String alo) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(alo);
        // 更新要计算的内容
        digest.update(msg.getBytes());
        // 完成哈希计算，得到摘要
        byte[] digestValue = digest.digest();
        System.out.println("Original message: " + msg + ", Digest Value(HEX): " + Hex.toHexString(digestValue));
    }

    /**
     * 生成密钥对
     * @param alo 生成密钥对使用的算法，可选算法：DiffieHellman(等同于：DH)、DSA、RSA
     * @return 密钥对
     * @throws NoSuchAlgorithmException 异常
     */
    public static KeyPair generateKeyPair(String alo) throws Exception {
        System.out.println("---------------begin generateKeyPair---------------");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(alo);
        System.out.println("Provider: " + keyPairGenerator.getProvider());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
        System.out.println("-----------------end generateKeyPair----------------");
        return keyPair;
    }

    /**
     * 生成ECC密钥对
     * @return 密钥对
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException 异常
     */
    public static KeyPair generateECCKeyPair() throws Exception {
        System.out.println("---------------begin generateECCKeyPair---------------");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        System.out.println("Provider: " + keyPairGenerator.getProvider());
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("Private key algorithm: " + privateKey.getAlgorithm());

        ECPublicKey ecPublicKey = (ECPublicKey)publicKey;
        ECPoint ecPoint = ecPublicKey.getW();
        BigInteger x = ecPoint.getAffineX();
        BigInteger y = ecPoint.getAffineY();
        System.out.println("基点坐标为（" + x + "," + y + ")");
        ECParameterSpec ecParameterSpec = ecPublicKey.getParams();
        System.out.println("辅因子： " + ecParameterSpec.getCofactor());
        EllipticCurve ellipticCurve = ecParameterSpec.getCurve();
        System.out.println("椭圆曲线参数： a=" + ellipticCurve.getA());
        System.out.println("椭圆曲线参数： b=" + ellipticCurve.getB());
        ECField ecField = ellipticCurve.getField();
        System.out.println("椭圆曲线参数： P=" + ecField.getFieldSize());

        ECPrivateKey ecPrivateKey = (ECPrivateKey)privateKey;
        System.out.println("私钥：k=" + ecPrivateKey.getS());
        System.out.println("---------------end generateECCKeyPair---------------");
        return keyPair;
    }

    /**
     * 读取RSA私钥文件(pkcs1标准格式的私钥, 且无口令保护, 参考测试用的文件)
     * @param pemFilePath 文件路径
     * @return RSA私钥
     * @throws GeneralSecurityException 异常
     * @throws IOException 异常
     */
    public static PrivateKey loadRSAPrivateKey(String pemFilePath) throws Exception {
        System.out.println("---------------begin pemFileLoadPrivateKeyPkcs1Encoded---------------");
        // PKCS#1 format
        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";
        Path path = Paths.get(pemFilePath);
        String privateKeyPem = new String(Files.readAllBytes(path));
        // java库解析时，只要中间的内容，头尾不要，换行也不要
        privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
        privateKeyPem = privateKeyPem.replaceAll("\\s", "");
        // 开始通过流来读取
        DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyPem));
        DerValue[] seq = derReader.getSequence(0);
        if (seq.length < 9) {
            throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
        }
        // skip version seq[0];
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
        System.out.println("---------------end pemFileLoadPrivateKeyPkcs1Encoded---------------");
        return privateKey;
    }

    /**
     * 从文件加载受口令保护的私钥(pkcs8标准格式，参考测试用的文件)
     * @param filePath 私钥文件路径(支持ECC、RSA)
     * @param password 口令
     * @return 私钥
     * @throws Exception 异常
     */
    public static PrivateKey loadPKCS8EncryptedPrivateKey(String filePath, String password) throws Exception {
        System.out.println("---------------begin loadEncryptedPrivateKey---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        Object pem = pemParser.readObject();
        if (pem instanceof PKCS8EncryptedPrivateKeyInfo) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
            PKCS8EncryptedPrivateKeyInfo keyInfo = (PKCS8EncryptedPrivateKeyInfo) pem;
            InputDecryptorProvider pkcs8Prov = new JceOpenSSLPKCS8DecryptorProviderBuilder()
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                    .build(password.toCharArray());
            PrivateKey privateKey = converter.getPrivateKey(keyInfo.decryptPrivateKeyInfo(pkcs8Prov));
            System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
            System.out.println("---------------end loadEncryptedPrivateKey---------------");
            return privateKey;
        } else {
            throw new Exception();
        }
    }

    /**
     * 从文件加载私钥（无口令保护）(pkcs8标准格式，参考测试用的文件)
     * @param filePath 私钥文件路径(支持ECC、RSA)
     * @return 私钥
     * @throws Exception 异常
     */
    public static PrivateKey loadPKCS8PrivateKey(String filePath) throws Exception {
        System.out.println("---------------begin loadPrivateKey---------------");
        try (PEMParser pemParser = new PEMParser(new FileReader(filePath))) {
            Object pem = pemParser.readObject();
            if (pem instanceof PrivateKeyInfo) {
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME);
                PrivateKeyInfo keyInfo = (PrivateKeyInfo) pem;
                PrivateKey privateKey = converter.getPrivateKey(keyInfo);
                System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
                System.out.println("---------------end loadPrivateKey---------------");
                return privateKey;
            }
            throw new RuntimeException("invalid key file.");
        }
    }

    /**
     * 从文件加载公钥
     * @param filePath 公钥文件路径(支持ECC、RSA)
     * @return 私钥
     * @throws IOException 异常
     */
    public static PublicKey loadPublicKey(String filePath) throws IOException {
        System.out.println("---------------begin loadPublicKey---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
        PublicKey publicKey = converter.getPublicKey(publicKeyInfo);
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end loadPublicKey---------------");
        return publicKey;
    }

    /**
     * 从文件加载公钥2
     * @param filePath 公钥文件路径(支持ECC和RSA)
     * @param alo 公钥对应的算法（EC、RSA）
     * @return 公钥
     * @throws Exception 异常
     */
    public static PublicKey loadPublicKey2(String filePath, String alo) throws Exception{
        System.out.println("---------------begin loadPublicKey2---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        PemObject pemObject = pemParser.readPemObject();
        byte[] content = pemObject.getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        KeyFactory factory = KeyFactory.getInstance(alo, BouncyCastleProvider.PROVIDER_NAME);
        PublicKey publicKey = factory.generatePublic(pubKeySpec);
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end loadPublicKey2---------------");
        return publicKey;
    }

    // todo 保存公钥对象的内容到文件中
    // todo 保存私钥对象的内容到文件中

    /**
     * 从证书中读取出公钥（含加载pem格式证书）
     * @param path 证书文件路径
     * @return 公钥
     * @throws CertificateException 异常
     * @throws FileNotFoundException 异常
     */
    public static PublicKey getPubKeyFromCert(String path) throws Exception {
        System.out.println("---------------begin get public key from CERT---------------");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(path));
        PublicKey publicKey = certificate.getPublicKey();
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end get public key from CERT---------------");
        return publicKey;
    }

    /**
     * 读取证书文件并加载成一个证书对象（复杂式写法）
     * @param path 证书存储路径
     * @param certEncodeType 证书编码类型，支持“PEM"和”DER“
     * @return 证书对象
     * @throws Exception 异常
     */
    public static X509Certificate loadCertFromFile(String path, String certEncodeType) throws Exception{
        System.out.println("---------------begin load CERT from file(" + certEncodeType + ")---------------");
        FileInputStream inputStream = new FileInputStream(path);
        int length = inputStream.available();
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        // 开始依据不同的编码类型来加载证书
        X509Certificate x509Certificate;
        if("PEM".equals(certEncodeType)) { // pem格式
            String crtStr = new String(bytes, StandardCharsets.UTF_8);
            crtStr = crtStr.replace("-----BEGIN CERTIFICATE-----", ""); // 为避免不同平台的回车换行问题，所以这里只匹配开头的文件类型描述
            crtStr = crtStr.replace("-----END CERTIFICATE-----", "");   // 为避免不同平台的回车换行问题，所以这里只匹配结尾的文件类型描述
            crtStr = crtStr.replaceAll("\n", "");      // 去掉换行
            crtStr = crtStr.replaceAll("\r", "");      // 去掉某些平台带有的回车
            x509Certificate = new X509CertImpl(Base64.getDecoder().decode(crtStr));
        } else { // der格式
            x509Certificate = new X509CertImpl(bytes);
        }
        System.out.println("Cert subject: " + x509Certificate.getSubjectX500Principal().getName());
        System.out.println("---------------end load CERT from file(" + certEncodeType + ")---------------");
        return x509Certificate;
    }

    /**
     * 生成密钥对并生成证书请求
     * @param alo 算法，可选：RSA、EC
     * @param csrInfos 证书请求的subject信息，除了emailAddress以外都必填
     * @return 证书请求对象
     * @throws Exception 异常
     */
    public static PKCS10CertificationRequest generateP10CertRequest(String alo, CsrInfos csrInfos) throws Exception {
        System.out.println("---------------begin generateP10CertRequest---------------");
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        String sigAlo; // 签名算法
        // 区分两种算法的密钥生成
        // RSA
        if (StringUtils.equals(alo, "RSA")){
            KeyPair keyPair = generateKeyPair("RSA");
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withRSA";
        } else { // ECC
            KeyPair keyPair = generateECCKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withECDSA";
        }
        // 申请者信息
        String subjectStr = "C=" + csrInfos.getCountry() + ",ST="+csrInfos.getState()
                + ",L=" + csrInfos.getLocal() + ",O=" + csrInfos.getOrganization()
                + ",OU=" + csrInfos.getOrganizationUnit() + ",CN="+csrInfos.getCommonName();
        if(StringUtils.isBlank(csrInfos.getEmailAddress())){
            subjectStr += (",E=" + csrInfos.getEmailAddress());
        }
        // 开始构造CSR
        X500Principal subject = new X500Principal(subjectStr);
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(sigAlo); // 签名算法
        ContentSigner signer = null;
        try {
            signer = csBuilder.build(privateKey);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
        PKCS10CertificationRequest csr = p10Builder.build(signer); // PKCS10的证书请求
        // 输出：1.2.840.113549.1.1.11 表示 SHA256withRSA
        // 输出： 1.2.840.10045.4.3.2 表示 SHA256withECDSA
        System.out.println("Signature Algorithm OID: " + csr.getSignatureAlgorithm().getAlgorithm().toString());
        System.out.println("Subject of PKCS10 Certification Request: " + csr.getSubject());
        System.out.println("---------------begin generateP10CertRequest---------------");
        return csr;
    }

    /**
     * 读取证书请求文件，并加载成一个证书请求对象
     * @param path 证书请求路径
     * @param csrEncodeType 证书请求文件的编码方式，支持“PEM”，“DER”
     * @return 证书请求对象
     * @throws Exception 异常
     */
    public static PKCS10CertificationRequest loadCsrFromFile(String path, String csrEncodeType) throws Exception{
        System.out.println("---------------begin load CSR from file(" + csrEncodeType + ")---------------");
        // 开始依据不同的编码类型来加载证书请求
        PKCS10CertificationRequest csr;
        if("PEM".equals(csrEncodeType)) { // pem格式
            PEMParser pemParser = new PEMParser(new FileReader(path));
            PemObject pemObject = pemParser.readPemObject();
            byte[] content = pemObject.getContent();
            csr = new PKCS10CertificationRequest(content);
        } else { // der格式
            FileInputStream inputStream = new FileInputStream(path);
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            inputStream.close();
            csr = new PKCS10CertificationRequest(bytes);
        }
        System.out.println("CSR subject: " + csr.getSubject().toString());
        System.out.println("---------------end load CSR from file(" + csrEncodeType + ")---------------");
        return csr;
    }

    /**
     * fixme 从证书请求中获取公钥(暂时没有找到相关的实现示例，也没有找到对应的方法)
     * @param csr 证书请求对象
     * @return 证书请求里的公钥
     * @throws IOException 异常
     */
    public static PublicKey getPubKeyFromCsr(PKCS10CertificationRequest csr) throws IOException {
        System.out.println("---------------begin get public key from CSR---------------");
        SubjectPublicKeyInfo subjectPublicKeyInfo= csr.getSubjectPublicKeyInfo();
        ASN1BitString pubKeyData = subjectPublicKeyInfo.getPublicKeyData();
        AsymmetricKeyParameter asymmetricKeyParameter = PublicKeyFactory.createKey(pubKeyData.getBytes());
        PublicKey publicKey = (PublicKey) asymmetricKeyParameter;
        System.out.println("Public key algorithm is " + publicKey.getAlgorithm());
        System.out.println("---------------end get public key from CSR---------------");
        return publicKey;
    }

    // todo 获取证书请求的信息
    public static void getCsrMsg(PKCS10CertificationRequest csr) {
        SubjectPublicKeyInfo subjectPublicKeyInfo= csr.getSubjectPublicKeyInfo();
        AlgorithmIdentifier algorithmIdentifier = subjectPublicKeyInfo.getAlgorithm();
        System.out.println("Public key algorithm identifier is " + algorithmIdentifier.getAlgorithm().toString());
    }

    /**
     * 颁发证书
     * @param csr 证书请求
     * @param issuerCertPath 用来颁发证书的父证书（通常是ca证书）
     * @param issuerPrvKeyPath 用于颁发证书的父证书对应的私钥
     * @param validDays 新证书的有效时间（单位：天）
     * @return 新的证书对象
     * @throws Exception 异常
     */
    public static X509Certificate issueCert(PKCS10CertificationRequest csr, String issuerCertPath,
                                            String issuerPrvKeyPath, long validDays) throws Exception {
        System.out.println("---------------begin issueCert---------------");
        Key caPrivateKey = null;
        CertificateFactory certFactory;
        X509Certificate caCert = null;

        certFactory = CertificateFactory.getInstance("X.509");
        caCert = (X509Certificate) certFactory.generateCertificate(new FileInputStream(issuerCertPath));  // 读取Ca证书
        caPrivateKey = loadRSAPrivateKey(issuerPrvKeyPath); // 读取私钥文件

        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA"); // ca的签名算法标识符
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId); // 摘要算法标识符
        assert caCert != null;
        X500Name issuer = new X500Name(caCert.getSubjectX500Principal().getName()); // 颁发者信息
        BigInteger serial = new BigInteger(32, new SecureRandom()); // 序列号
        Date from = new Date(); // 证书起始生效时间
        Date to = new Date(System.currentTimeMillis() + validDays * 24 * 60 * 60 * 1000); // 证书失效时间

        // 使用x509来组装证书
        X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(issuer, serial, from, to,
                csr.getSubject(), csr.getSubjectPublicKeyInfo());
        // CA端进行签名, 才有具有法律效力
        X509Certificate cert = null;
        ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(PrivateKeyFactory.createKey(caPrivateKey.getEncoded()));
        // 生成BC结构的证书
        Security.addProvider(new BouncyCastleProvider());
        cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(signer));

        System.out.println("新证书的版本： " + cert.getVersion());
        System.out.println("新证书的subject: " + cert.getSubjectX500Principal().getName());
        System.out.println("新证书的颁发者subject: " + cert.getIssuerDN().getName());
        System.out.println("---------------end issueCert---------------");
        return cert;
    }

    /**
     * todo 从der格式(为了方便传递，一般der格式内容会转为十六进制串来传递)转换成pem格式
     * @param der 一个Hex-String形式的der格式证书请求内容
     * @param type 文件类型，支持“CSR","CERT"
     * @return pem格式的字符串
     */
    public static String csrDer2pem(String der, String type){
        System.out.println("---------------begin (" + type + ") DER to PEM---------------");
        PemObject pemObject;
        if("CSR".equals(type)) {
            pemObject = new PemObject("CERTIFICATE REQUEST", der.getBytes(StandardCharsets.UTF_8));
        } else{
            pemObject = new PemObject("CERTIFICATE", der.getBytes(StandardCharsets.UTF_8));
        }
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
        try {
            pemWriter.writeObject(pemObject);
            pemWriter.flush();
        } catch (IOException e) {}
        finally {
            try {
                pemWriter.close();
            } catch (IOException e) {}
        }
        String pem = stringWriter.toString();
        System.out.println(type + " in pem: " + pem);
        System.out.println("---------------begin (" + type + ") DER to PEM---------------");
        return pem;
    };

    /**
     * todo 将传进来的pem格式证书字符串转为der格式（Hex-String）
     * @param crtStr pem格式证书字符串
     * @return der格式（Hex-String）证书
     * @throws Exception 异常
     */
    public static String certPem2DerHexStr(String crtStr) throws Exception {
        System.out.println("---------------begin CERT PEM to DER---------------");
        // 去除头部尾部
        crtStr = crtStr.replace("-----BEGIN CERTIFICATE-----\n", "");
        crtStr = crtStr.replace("\n-----END CERTIFICATE-----\n", "");
        // 这种写法的话，要防止内容转为二进制后，里面附带换行
        crtStr = crtStr.replaceAll("\n","");
        // 转为证书对象
        X509Certificate x509Certificate = new X509CertImpl(Base64.getDecoder().decode(crtStr));
        byte[] bytes = x509Certificate.getEncoded();
        // 获得der格式编码
        String certDER = Hex.toHexString(bytes);
        System.out.println("CERT in der(Hex-String): " + certDER);
        System.out.println("---------------end CERT PEM to DER---------------");
        return certDER;
    }

    /**
     * 依据传入的证书对象，获取证书的所有信息
     * @param x509Certificate 证书对象
     * @throws IOException 异常
     */
    public static void getCertMsg(X509Certificate x509Certificate) throws IOException {
        System.out.println("---------------begin get certificate message---------------");
        System.out.println("Cert version is " + x509Certificate.getVersion());
        System.out.println("Cert serial number: " + x509Certificate.getSerialNumber());
        System.out.println("Cert sign algorithm is " + x509Certificate.getSigAlgName());
        System.out.println("Cert hash value is " + Hex.toHexString(x509Certificate.getSignature()));

        Date notBefore = x509Certificate.getNotBefore();
        Date notAfter = x509Certificate.getNotAfter();
        System.out.println("Cert valid time is from " + notBefore.toString() + " to " + notAfter.toString());

        System.out.println("Subject: " + x509Certificate.getSubjectX500Principal().getName());
        System.out.println("Issuer subject: " + x509Certificate.getIssuerX500Principal().getName());
        sun.security.x509.X500Name x500Name = sun.security.x509.X500Name.asX500Name(x509Certificate.getSubjectX500Principal());
        System.out.println("Common Name(CN): " + x500Name.getCommonName());
        System.out.println("Country(C): " + x500Name.getCountry());
        System.out.println("State(S): " + x500Name.getState());
        System.out.println("Locality(L): " + x500Name.getLocality());
        System.out.println("Organization(O): " + x500Name.getOrganization());
        System.out.println("Organization Unit(OU): " + x500Name.getOrganizationalUnit());
        // 由于邮箱地址实际上创建证书请求时是选填的，所以好像没有提供获取EmailAddress(E)的方法
        sun.security.x509.X500Name issuerX500Name = sun.security.x509.X500Name.asX500Name(x509Certificate.getIssuerX500Principal());
        System.out.println("Issuer CN: " + issuerX500Name.getCommonName()); // 颁发者的subject信息获取方式同自身，所以这里不做重复
        System.out.println("---------------end get certificate message---------------");
    }

    // todo 私钥加密
    // todo 私钥解密
    // todo 公钥加密
    // todo 公钥解密
    // todo 生成p12
    // todo 解析p12
    // todo 验证一个公钥和一个私钥是否匹配
    // todo 验证一个私钥与一本证书是否匹配
    // todo 验证证书链是否正确
}
