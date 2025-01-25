package com.basic.happytest.modules.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Tomcat配置定制器
 * @author lhf
 */

@Component
public class TomcatConfigCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Autowired
    private ServerProperties serverProperties;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        /**
         * 以下为打印Tomcat配置信息
         */
        System.out.println("Tomcat default configuration:");
        System.out.println("Port: " + serverProperties.getPort());
        System.out.println("Context Path: " + serverProperties.getServlet().getContextPath());
        System.out.println("Compression status: " + serverProperties.getCompression().getEnabled());
        System.out.println("Compression min response size: " + serverProperties.getCompression().getMinResponseSize());
        System.out.println("Compression mime types: " + Arrays.toString(serverProperties.getCompression().getMimeTypes()));
    }
}

