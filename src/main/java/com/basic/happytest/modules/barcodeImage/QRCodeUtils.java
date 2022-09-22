package com.basic.happytest.modules.barcodeImage;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.bouncycastle.util.encoders.Hex;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具
 * @author lhf
 * 参考：Java实现二维码生成 Google-Zxing：https://blog.csdn.net/gisboygogogo/article/details/86036656 <br/>
 * 参考：java实现识别二维码图片功能: https://blog.csdn.net/weijx_/article/details/108670580<br/>
 */

public class QRCodeUtils {

    /**
     * PNG图片格式
     */
    public static final String IMAGE_FORMAT_PNG = "PNG";

    /**
     * JPG图片格式
     */
    public static final String IMAGE_FORMAT_JPG = "JPG";

    /**
     * 生成二维码图片工具方法
     * @param text 二维码内容（如果内容很短，似乎会导致二维码扫描不出来）（网友描述，内容太长也会出问题）
     * @param width 二维码图片宽度
     * @param height 二维码图片高度
     * @param filePath 图片保存路径
     * @param imageFormat 图片格式
     * @throws Exception 异常
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath, String imageFormat)
            throws Exception {
        File file = new File(filePath);
        if(file.exists()){
            throw new Exception("输出的文件路径已有文件，请先删除");
        }
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        // 指定要使用的纠错程度（容错率）（不同BarcodeFormat可能不一样），H=最多30%的字码可被修正，L=7%,M=15%,Q=25%
        // 举个例子假设一个二维码有30%以下的面积被遮盖或者去除时，二维码扫描器依然能够从这个残缺的二维码中准确获取信息。
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 不设置UTF-8的话，text内的中文会是乱码
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        // 指定生成条形码时要使用的边距（以像素为单位）
        hints.put(EncodeHintType.MARGIN, 1);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, imageFormat, path);
    }

    /**
     * 生成二维码图片字节数组
     * @param text 内容（如果内容很短，似乎会导致二维码扫描不出来）（网友描述，内容太长也会出问题）
     * @param width 二维码图片宽度
     * @param height 二维码图片高度
     * @param imageFormat 图片格式
     * @return 二维码图片字节数组
     * @throws Exception 异常
     */
    public static byte[] generateQRCodeImageBytes(String text, int width, int height, String imageFormat) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        // 指定要使用的纠错程度（容错率）（不同BarcodeFormat可能不一样），H=最多30%的字码可被修正，L=7%,M=15%,Q=25%
        // 举个例子假设一个二维码有30%以下的面积被遮盖或者去除时，二维码扫描器依然能够从这个残缺的二维码中准确获取信息。
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 不设置UTF-8的话，text内的中文会是乱码
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        // 指定生成条形码时要使用的边距（以像素为单位）
        hints.put(EncodeHintType.MARGIN, 1);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        System.out.println(Hex.toHexString(bytes));
        return bytes;
    }

    /**
     * 解析二维码图片并获得内容
     * @param path 二维码图片路径
     * @return 二维码图片的内容
     * @throws Exception 异常
     */
    public static String decodeQRCodeImage(String path) throws Exception{
        File file = new File(path);
        if(!file.exists()) {
            throw new Exception("图片不存在");
        }
        BufferedImage bufferedImage = ImageIO.read(file);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        Result result = new MultiFormatReader().decode(binaryBitmap, hints); //解码
        String content = result.getText();
        System.out.println("图片中内容：  ");
        System.out.println("content： " + content);
        return content;
    }
}
