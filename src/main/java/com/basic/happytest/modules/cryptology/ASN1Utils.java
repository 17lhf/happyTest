package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.math.RadixUtils;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * ASN.1处理
 * @author lhf
 */

public class ASN1Utils {

    /**
     * 解析并打印ASN1格式的RSA公钥信息
     * @param pubKeyHex ASN1格式公钥的十六进制保存字符串
     * @param isPkcs8 true-是PCKS8标准，false-是PCKS1标准
     */
    public static void printParseASN1RsaPubKey(String pubKeyHex, boolean isPkcs8) {
        int nowIndex = 0;
        if (isPkcs8) {
            String sequenceTip1 = pubKeyHex.substring(nowIndex, nowIndex + 2);
            nowIndex += 2;
            System.out.println("Sequence Identifier：" + sequenceTip1);
            // 数据长度 < 128：则 Length 的 8bit首位为0，其他7位表示数据长度
            // 数据长度 >= 128：则 Length 的第一个8bit为 0x8?，其中 ? 是后面跟的是长度。比如 0x81 表示后面一个字节为长度，如果是 0x82 则表示后面两个字节为长度
            // 开始尝试测试数据长度标识
            String lengthTip1 = pubKeyHex.substring(nowIndex, nowIndex + 1);
            nowIndex += 1;
            System.out.println("内容长度的标识（即将开始校验）：" + lengthTip1);
            // 若数据长度 >= 128
            if (RadixUtils.hexIsFirstBitOne(lengthTip1)) {
                // 此时说明 表示数据长度的字节数 为下一位字符
                String lenLenHex1 = pubKeyHex.substring(nowIndex, nowIndex + 1);
                int lenLen1 = Integer.parseInt(lenLenHex1, 16);
                nowIndex += 1;
                System.out.println("表示数据长度的字节数, 十六进制表示为：" + lenLenHex1 + ", 十进制表示为：" + lenLen1);
                String length1 = pubKeyHex.substring(nowIndex, nowIndex + lenLen1 * 2);
                nowIndex += lenLen1 * 2;
                System.out.println("内容长度，十六进制表示为：" + length1 + ", 十进制表示为：" + Integer.parseInt(length1, 16));
            } else {
                System.out.println("内容长度，十六进制表示为：" + lengthTip1 + ", 十进制表示为：" + Integer.parseInt(lengthTip1, 16));
            }

            String sequenceTip2 = pubKeyHex.substring(nowIndex, nowIndex + 2);
            nowIndex += 2;
            System.out.println("Sequence Identifier：" + sequenceTip2);
            String sequenceLen2 = pubKeyHex.substring(nowIndex, nowIndex + 2);
            nowIndex += 2;
            System.out.println("Sequence长度，十六进制表示为：" + sequenceLen2 + ", 十进制表示为：" + Integer.parseInt(sequenceLen2, 16));
            String objectIdentifier = pubKeyHex.substring(nowIndex, nowIndex + 2);
            nowIndex += 2;
            System.out.println("OID标识：" + objectIdentifier);
            String length2 = pubKeyHex.substring(nowIndex, nowIndex + 2);
            nowIndex += 2;
            System.out.println("内容长度，十六进制表示为：" + length2 + ", 十进制表示为：" + Integer.parseInt(length2, 16));
            String objId = pubKeyHex.substring(nowIndex, nowIndex + 18);
            nowIndex += 18;
            // 1.2.840.113549.1.1是RSA算法的标识符，而42 134 72 134 247 13 1 1是该算法的OID（Object Identifier）表示方式 todo 不知道两种oid表示方式为什么相等
            System.out.println("ObjectId: " + objId);

            String nullStr = pubKeyHex.substring(nowIndex, nowIndex + 4);
            nowIndex += 4;
            System.out.println("无意义串：" + nullStr);

            String bitStringTip = pubKeyHex.substring(nowIndex, nowIndex + 2);
            nowIndex += 2;
            System.out.println("bitString的标识：" + bitStringTip);
            // 开始尝试测试数据长度标识
            String lengthTip3 = pubKeyHex.substring(nowIndex, nowIndex + 1);
            nowIndex += 1;
            System.out.println("内容长度的标识（即将开始校验）：" + lengthTip3);
            // 若数据长度 >= 128
            if (RadixUtils.hexIsFirstBitOne(lengthTip3)) {
                // 此时说明 表示数据长度的字节数 为下一位字符
                String lenLenHex3 = pubKeyHex.substring(nowIndex, nowIndex + 1);
                int lenLen3 = Integer.parseInt(lenLenHex3, 16);
                nowIndex += 1;
                System.out.println("表示数据长度的字节数, 十六进制表示为：" + lenLenHex3 + ", 十进制表示为：" + lenLen3);
                String length3 = pubKeyHex.substring(nowIndex, nowIndex + lenLen3 * 2);
                nowIndex += lenLen3 * 2;
                System.out.println("内容长度，十六进制表示为：" + length3 + ", 十进制表示为：" + Integer.parseInt(length3, 16));
            } else {
                System.out.println("内容长度，十六进制表示为：" + lengthTip3 + ", 十进制表示为：" + Integer.parseInt(lengthTip3, 16));
            }

            String paddingBitNum = pubKeyHex.substring(nowIndex, nowIndex + 2);
            nowIndex += 2;
            System.out.println("填充比特数：" + paddingBitNum);
        }

        String sequenceTip4 = pubKeyHex.substring(nowIndex, nowIndex + 2);
        nowIndex += 2;
        System.out.println("Sequence Identifier：" + sequenceTip4);
        // 开始尝试测试数据长度标识
        String lengthTip4 = pubKeyHex.substring(nowIndex, nowIndex + 1);
        nowIndex += 1;
        System.out.println("内容长度的标识（即将开始校验）：" + lengthTip4);
        // 若数据长度 >= 128
        if (RadixUtils.hexIsFirstBitOne(lengthTip4)) {
            // 此时说明 表示数据长度的字节数 为下一位字符
            String lenLenHex4 = pubKeyHex.substring(nowIndex, nowIndex + 1);
            int lenLen4 = Integer.parseInt(lenLenHex4, 16);
            nowIndex += 1;
            System.out.println("表示数据长度的字节数, 十六进制表示为：" + lenLenHex4 + ", 十进制表示为：" + lenLen4);
            String length4 = pubKeyHex.substring(nowIndex, nowIndex + lenLen4 * 2);
            nowIndex += lenLen4 * 2;
            System.out.println("内容长度，十六进制表示为：" + length4 + ", 十进制表示为：" + Integer.parseInt(length4, 16));
        } else {
            System.out.println("内容长度，十六进制表示为：" + lengthTip4 + ", 十进制表示为：" + Integer.parseInt(lengthTip4, 16));
        }

        String dataType = pubKeyHex.substring(nowIndex, nowIndex + 2);
        nowIndex += 2;
        System.out.println("数据类型：" + dataType + ", 02表示整数");
        // 开始尝试测试数据长度标识
        String lengthTip5 = pubKeyHex.substring(nowIndex, nowIndex + 1);
        nowIndex += 1;
        System.out.println("内容长度的标识（即将开始校验）：" + lengthTip5);
        String moduleLen;
        int moduleLenDigital;
        // 若数据长度 >= 128
        if (RadixUtils.hexIsFirstBitOne(lengthTip5)) {
            // 此时说明 表示数据长度的字节数 为下一位字符
            String lenLenHex5 = pubKeyHex.substring(nowIndex, nowIndex + 1);
            int lenLen5 = Integer.parseInt(lenLenHex5, 16);
            nowIndex += 1;
            System.out.println("表示模长度的字节数, 十六进制表示为：" + lenLenHex5 + ", 十进制表示为：" + lenLen5);
            moduleLen = pubKeyHex.substring(nowIndex, nowIndex + lenLen5 * 2);
            nowIndex += lenLen5 * 2;
        } else {
            moduleLen = lengthTip5;
            System.out.println("内容长度，十六进制表示为：" + lengthTip5 + ", 十进制表示为：" + Integer.parseInt(lengthTip5, 16));
        }
        moduleLenDigital = Integer.parseInt(moduleLen, 16);
        System.out.println("模长度，十六进制表示为：" + moduleLen + ", 十进制表示为：" + moduleLenDigital);
        String module = pubKeyHex.substring(nowIndex, nowIndex + moduleLenDigital * 2);
        nowIndex += 2 * moduleLenDigital;
        // ASN.1 规定整型 INTEGER 需要支持正整数、负整数和零。BER/DER 使用大端模式存储 INTEGER，并通过最高位来编码正负数(最高位0为正数，1为负数)。
        // 如果密钥参数值最高位为 1，则 BER/DER 编码会在参数前额外加 0x00 以表示正数，这也就是为什么有时候密钥参数前面会多出1字节 0x00 的原因。
        System.out.println("模，十六进制表示为：" + module + "，十进制表示为：" + new BigInteger(module, 16).toString(10));

        String dataType2 = pubKeyHex.substring(nowIndex, nowIndex + 2);
        nowIndex += 2;
        System.out.println("数据类型：" + dataType2 + ", 02表示整数");
        String exponentLen = pubKeyHex.substring(nowIndex, nowIndex + 2);
        nowIndex += 2;
        int exponentLenDigital = Integer.parseInt(exponentLen, 16);
        System.out.println("公钥指数长度，十六进制表示为：" + exponentLen + ", 十进制表示为：" + exponentLenDigital);
        String exponent = pubKeyHex.substring(nowIndex, nowIndex + exponentLenDigital * 2);
        // nowIndex += 2 * exponentLenDigital;
        System.out.println("模，十六进制表示为：" + exponent + "，十进制表示为：" + new BigInteger(exponent, 16).toString(10));
    }

    /**
     * OID转DER格式字符串 <br />
     * 目前作者实际应用，更多是在手动设置 增强型密钥用法 的值的时候
     * @param oidList 多个oid
     * @return DER格式内容
     */
    public static byte[] oid2DERString(List<String> oidList) throws GSSException, IOException {
        ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (String oid : oidList) {
            Oid oid1 = new Oid(oid);
            asn1EncodableVector.add(ASN1ObjectIdentifier.getInstance(oid1.getDER()));
        }
        DERSequence derSequence = new DERSequence(asn1EncodableVector);
        return derSequence.getEncoded();
    }
}
