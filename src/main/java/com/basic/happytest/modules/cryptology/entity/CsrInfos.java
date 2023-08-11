package com.basic.happytest.modules.cryptology.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 获取申请者信息 <br />
     * 注意，这里信息的字段拼装顺序，最好和你的CA链其他证书是一致的，通常应该是和Openssl或者加密机的默认顺序一致，即E、CN、OU、O、L、S、C
     * @return 申请者信息
     */
    public String getSubject() {
        String subjectStr = "";
        if(StringUtils.isNotBlank(emailAddress)){
            subjectStr += ("EmailAddress=" + emailAddress + ",");
        }
        subjectStr += ("CN=" + commonName + ",OU=" + organizationUnit + ",O=" + organization + ",L="
                + local + ",ST=" + state + ",C=" + country);
        return subjectStr;
    }
}
