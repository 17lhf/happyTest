package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.ProviderEnums;
import com.sun.crypto.provider.SunJCE;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

/**
 * 加解密相关工具
 * @author lhf
 */

public class EncAndDecUtils {

    // 解决报错：no such provider: BC
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密（对长度有要求）<br/>
     * RSA算法原理上，其实要求的明文和密文都是： 0<明文或密文大小<密钥的模大小，其实也对应了我们常说的“0<明文或密文的长度<密钥的模的长度”(长度相等时要额外多一步比较大小)，
     * 实际上一些加密工具之类的会对明文长度为0的时候进行特殊处理，
     * 另外，虽然最大可被加密的明文长度是密钥的长度，但是具体的实现工具或规范(如PKCS规范)都有进行限制（特别是当有填充时）。
     * 所以具体情况要看你用的工具/库的限制是怎样的。
     * 密文长度也要看工具/库，通常情况下都是模的长度，
     * <br/>
     * Java 默认的 RSA加密实现不允许明文长度超过密钥长度(单位是字节，也就是 byte)。
     * 也就是说，如果我们定义的密钥(如：java.security.KeyPairGenerator.initialize(int keySize) 来定义密钥长度)长度为 1024(单位是位，也就是 bit)。
     * 生成的密钥长度就是 1024位 /（8位/字节） = 128字节，那么我们需要加密的明文长度不能超过 128字节。
     * 也就是说，我们最大能将 128 字节长度的明文进行加密，否则会抛异常<br/>
     * 实测“RSA/ECB/PKCS1Padding”时，2048RSA是最大245，其实PKCS1Padding这种填充方式本来就是要占用至少11的长度，也就是明文只能是256-11=245了。
     * <br/>
     * BC库的话，也是密钥长度， 如128，256.超过的话，报错：org.bouncycastle.crypto.DataLengthException: input too large for RSA cipher。
     * 但是，上面BC库时所说的，都是NoPadding的情况，如果有Padding，则blockSize大小就不一样。
     * 具体限制可见org.bouncycastle.crypto.AsymmetricBlockCipher的各个实现类的getInputBlockSize方法
     * <br/>
     * 正常情况下，NoPadding占用大小是0，也就是你提供的明文可以是当前密钥最大的支持长度。PKCS1Padding占用的位数大小是11，也就是明文长度只能
     * 是当前密钥最大支持长度-11
     * <p>
     * 关于填充：<br/>
     * OAEP is less vulnerable to padding oracle attacks than PKCS#1 v1.5 padding. GCM is also protected against padding oracle attacks。<br/>
     * 其实ECB是对称密码的一种分组模式，对非对称密钥没啥用，其实用None就行，例如RSA/None/PKCS1Padding，像RSA加密是不对数据进行分组的。<br/>
     * 但是，实测发现，sun库要求得带ECB，None的话，得用BC库才支持<br/>
     * </p>
     * 注意：如果是ECC，则只能用公钥加密，不支持私钥加密数据 <br/>
     * 注意：ECC本身并没有真正定义任何加密/解密操作，构建在椭圆曲线上的算法确实如此（todo 待确认） <br/>
     * @param key 用来加密的密钥
     * @param alo 密钥对应的算法/模式/填充模式，支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *            “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“、
     *            “ECIES"(ECIES表示ECC)。
     * @param data 等待被加密的数据，数据不能太长
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 密文数据
     * @throws Exception 异常
     */
    public static byte[] encryptData(Key key, String alo, byte[] data, String provider) throws Exception{
        System.out.println("---------------begin encrypt data---------------");
        System.out.println("alo is: " + alo);
        System.out.println("data length is: " + data.length);
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(ProviderEnums.BC.getProvider().equals(provider) || ProviderEnums.SUN.getProvider().equals(provider)){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        System.out.println("Provider: " + cipher.getProvider());
        // 使用默认的随机数生成器（未指定第三个参数）
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] res = cipher.doFinal(data);
        System.out.println("Hex String type encrypted result data is: " + Hex.toHexString(res));
        System.out.println("Base64 String type encrypted result data is: " + Base64.getEncoder().encodeToString(res));
        System.out.println("---------------end encrypt data---------------");
        return res;
    }

    /**
     * 解密（对长度有要求，例如2048长度的RSA只能解密256长度的数据，1024的RSA只能解密128长度的数据）<br/>
     * <p>
     *     BC库的输出大小具体可见org.bouncycastle.crypto.AsymmetricBlockCipher的各个实现类的getOutputBlockSize方法
     * </p>
     * 注意：如果是ECC，则只能用私钥解密，不支持公钥解密数据<br/>
     * 注意：ECC本身并没有真正定义任何加密/解密操作，构建在椭圆曲线上的算法确实如此（todo 待确认）<br/>
     * @param key 用来解密的密钥
     * @param alo 密钥对应的算法/模式/填充模式，要和加密时使用的配置一致，
     *            支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *            “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“、
     *            “ECIES"(ECIES表示ECC)
     * @param encData 等待被解密的密文数据
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 明文数据
     * @throws Exception 异常
     */
    public static byte[] decryptData(Key key, String alo, byte[] encData, String provider) throws Exception{
        System.out.println("---------------begin decrypt data---------------");
        System.out.println("alo is: " + alo);
        System.out.println("encrypted data length is: " + encData.length);
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(provider.equals(BouncyCastleProvider.PROVIDER_NAME) || provider.equals(new SunJCE().getName())){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println("Provider: " + cipher.getProvider());
        byte[] res = cipher.doFinal(encData);
        System.out.println("Hex String type decrypt result data is: " + Hex.toHexString(res));
        System.out.println("Base64 String type decrypt result data is: " + Base64.getEncoder().encodeToString(res));
        System.out.println("---------------end decrypt data---------------");
        return res;
    }

    /**
     * 分段加密（仅支持RSA）<br/>
     * 更多内容，详见encryptData方法的说明<br/>
     * <br/>
     * 另外，每次加密生成密文的长度等于密钥长度，如1024bit的密钥，加密一次出来的密文长度是128字节（byte），
     * 所以分段加密的最终的密文长度，必然是密钥长度的正整数倍（如：256字节、512字节）<br/>
     * @param key 用于加密的密钥
     * @param data 待加密的数据
     * @param alo 密钥对应的算法/模式/填充模式，支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *      “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“)
     * @param blockSize 分段的块的大小，如果密钥大小是2048，NoPadding时最大的块是256，如果是1024则最大的块是128。否则会报错。
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 加密后的密文
     * @throws Exception 异常
     */
    public static byte[] rsaBlockEncrypt(byte[] data, String alo, Key key, int blockSize, String provider) throws Exception {
        System.out.println("---------------begin rsa block encrypt data---------------");
        System.out.println("Key algorithm is: " + key.getAlgorithm());
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(provider.equals(BouncyCastleProvider.PROVIDER_NAME) || provider.equals(new SunJCE().getName())){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        // 使用加密模式，并传入密钥
        cipher.init(Cipher.ENCRYPT_MODE, key);
        System.out.println("Provider: " + cipher.getProvider());
        // 获取Cipher块的大小（以字节为单位），如果基础算法不是块 cipher，则返回 0
        // 如果用的是java原生的，则是0
        // 如果用的是BC库，则2048的RSA默认是255, 1024的RSA默认是127。所以这时候其实也可以就直接使用这个默认值。如：int blockSize = cipher.getBlockSize()
        // BC库，使用“RSA/ECB/PKCS1Padding”，则2048RSA默认是245
        System.out.println("Cipher default block size: " + cipher.getBlockSize());
        // 在给定了输入长度 inputLen（以字节为单位）的情况下，返回用于保存下一个 update 或 doFinal 操作结果所需的输出缓冲区长度的字节数。
        // 此调用还考虑到来自上一个 update 调用的未处理（已缓存）数据和填充。
        // 下一个 update 或 doFinal 调用的实际输出长度可能小于此方法返回的长度。
        // 2048的RSA则对应的是256, 1024的RSA对应的是128
        System.out.println("Cipher output size: " + cipher.getOutputSize(cipher.getBlockSize()));
        // 避免等下陷入死循环等问题
        if(blockSize < 1){
            throw new Exception("Block size can't be less than 1!");
        }
        // 分段加密
        int inputLen = data.length;
        // 偏移量
        int offLen = 0;
        // 暂存结果
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        // 开始循环加密
        while(inputLen - offLen > 0) {
            byte [] cache;
            if(inputLen - offLen > blockSize) {
                cache = cipher.doFinal(data, offLen, blockSize);
            } else {
                cache = cipher.doFinal(data, offLen,inputLen - offLen);
            }
            bops.write(cache);
            offLen += blockSize;
        }
        bops.close();
        // 获得加密结果
        byte[] encryptedData = bops.toByteArray();
        System.out.println("Encrypted data in hex format: " + Hex.toHexString(encryptedData));
        System.out.println("---------------end rsa block encrypt data---------------");
        return encryptedData;
    }

    /**
     * 分段解密（仅支持RSA）<br/>
     * 更多内容，详见encryptData方法的说明<br/>
     * @param encData 待解密的密文
     * @param alo 密钥对应的算法/模式/填充模式，支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *       “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“)
     *       要求必须与加密时使用的一致，否则解密结果会与原文不匹配
     * @param key 与加密密钥相对应的解密的密钥
     * @param blockSize 解密分块的大小，如果密钥大小是2048则块大小是256，如果是1024则最大的块是128。
     *                  此处的256和128其实对应的就是加密时每段被加密后的密文的大小。
     *                  如果是用BC库，若输入的size不是解密分块大小，则不会报错，但是解密结果与原文对不上。
     *                  如果是用JAVA库，若输入的size不是解密分块大小，则doFinal会报错javax.crypto.BadPaddingException: Decryption error。
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 解密后的明文
     * @throws Exception 异常
     */
    public static byte[] rsaBlockDecrypt(byte[] encData, String alo, Key key, int blockSize, String provider) throws Exception {
        System.out.println("---------------begin rsa block decrypt data---------------");
        System.out.println("Key algorithm is: " + key.getAlgorithm());
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(provider.equals(BouncyCastleProvider.PROVIDER_NAME) || provider.equals(new SunJCE().getName())){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE库
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println("Provider: " + cipher.getProvider());
        int inputLen = encData.length;
        // 避免等下陷入死循环等问题
        if(blockSize < 1){
            throw new Exception("Block size can't be less than 1!");
        } else if(inputLen % blockSize != 0){ // 正常被RSA加密后的密文，肯定是单次加密结果的整数倍，也就是解密时要分块的整数倍
            throw new Exception("Data length is error!");
        }
        // 偏移量
        int offLen = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > blockSize){
                cache = cipher.doFinal(encData, offLen, blockSize);
            } else{
                cache = cipher.doFinal(encData, offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            offLen += blockSize;
        }
        byteArrayOutputStream.close();
        byte[] plainData = byteArrayOutputStream.toByteArray();
        System.out.println("Plain data in hex format: " + Hex.toHexString(plainData));
        System.out.println("---------------end rsa block decrypt data---------------");
        return plainData;
    }
}
