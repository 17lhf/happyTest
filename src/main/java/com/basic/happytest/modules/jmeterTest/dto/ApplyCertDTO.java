package com.basic.happytest.modules.jmeterTest.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * JMeter 编写脚本测试-申请证书接口入参
 * @author : lhf
 */

@Getter
@Setter
public class ApplyCertDTO {

    private String cn;

    private String csr;
}
