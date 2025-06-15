package com.basic.happytest.modules.jmeterTest;

import com.basic.happytest.modules.jmeterTest.dto.ApplyCertDTO;
import com.basic.happytest.modules.jmeterTest.dto.ThanksDTO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringReader;

/**
 * 配合 JMeter 编写脚本而生的控制器
 * @author : lhf
 */

@RestController
@RequestMapping("/jmeter")
public class JMeterController {

    private static final Logger  logger = LoggerFactory.getLogger(JMeterController.class);

    @RequestMapping(value = "/apply-cert-check", method = RequestMethod.POST)
    public String applyCertCheck(@RequestBody ApplyCertDTO dto) throws IOException {
        if (dto == null || StringUtils.isAnyBlank(dto.getCn(),  dto.getCsr())) {
            throw new RuntimeException("参数缺失");
        }
        PEMParser pemParser = new PEMParser(new StringReader(dto.getCsr()));
        PemObject pemObject = pemParser.readPemObject();
        byte[] content = pemObject.getContent();
        PKCS10CertificationRequest pkcs10Csr = new PKCS10CertificationRequest(content);
        if (!pkcs10Csr.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString().equals(dto.getCn())) {
            throw new RuntimeException("CSR格式错误");
        }
        logger.info("apply cert cn: {}", dto.getCn());
        return dto.getCn() + "，格式正确";
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.POST)
    public String thanks(@RequestBody ThanksDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getContent())) {
            throw new RuntimeException("参数缺失");
        }
        logger.info("thanks content: {}", dto.getContent());
        return dto.getContent() + "，不用谢";
    }

}
