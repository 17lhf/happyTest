package com.basic.happytest.modules.barcodeImage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QRCodeUtilsTest {

    @Test
    void generateQRCodeImage() throws Exception {
        String text = "预约成功！同时，愚人节快乐！";
        QRCodeUtils.generateQRCodeImage(text, 500, 500, "", QRCodeUtils.IMAGE_FORMAT_PNG);
    }
}