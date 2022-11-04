package com.basic.happytest.modules.feignClient;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 伪装客户端访问其他服务
 * @author lhf
 */

public class FeignClient {

    /**
     * 请求重试机制
     */
    private final Retryer retryer;

    /**
     * 连接超时时长，单位: s
     */
    private static final long CONNECT_TIMEOUT = 10L;

    /**
     * 读超时时长，单位：s
     */
    private static final long READ_TIMEOUT = 30L;

    /**
     * 访问服务的根路径
     */
    private final String rootUri;

    public FeignClient(String rootUri) {
        this.rootUri = rootUri;
        // 重试机制设置为不重试
        this.retryer = Retryer.NEVER_RETRY;
    }

    public <T> T getService(Class<T> tClass) {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .retryer(retryer) // 这一行注释掉会发现，Feign默认会重试4次，加上本来的第1次，总计5次请求才结束
                // BASIC 时，只打印请求路径和应答HTTP情况。 HEADERS时除了请求体和应答体都打印了
                // NONE 时，啥都不打印。 FULL是啥都打印。
                .logLevel(Logger.Level.HEADERS)
                .logger(new FeignLog(FeignClient.class))
                .client(ssLClient())
                .options(new Request.Options(CONNECT_TIMEOUT, TimeUnit.SECONDS,
                        READ_TIMEOUT, TimeUnit.SECONDS, true))
                .target(tClass, rootUri);
    }

    /**
     * 自定义SSL校验方式，Https请求时会用上，不影响HTTP的请求
     * @return 自定义SSL校验方式的Client
     */
    public Client ssLClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            return new Client.Default(ctx.getSocketFactory(), (hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SSL通讯校验失败");
            return null;
        }
    }

    /**
     * 自定义日志打印
     */
    final class FeignLog extends Logger {
        private final org.slf4j.Logger logger;

        public FeignLog(Class<?> clazz) {
            logger = LoggerFactory.getLogger(clazz);
        }

        @Override
        protected void log(String configKey, String format, Object... args) {
            logger.info(String.format(methodTag(configKey) + format, args));
        }
    }
}
