package com.basic.happytest.modules.cryptology;

import lombok.Getter;
import lombok.Setter;

/**
 * 证书请求的subject相关字段
 * @author lhf
 */

@Setter
@Getter
public class CsrInfos {
    private String country;

    private String state;

    private String local;

    private String organization;

    private String organizationUnit;

    private String emailAddress;

    private String commonName;
}
