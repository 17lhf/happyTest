package com.basic.happytest.modules.feignClient;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
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
                .requestInterceptor(new BeforeRequest("nothingStr"))
                .options(new Request.Options(CONNECT_TIMEOUT, TimeUnit.SECONDS,
                        READ_TIMEOUT, TimeUnit.SECONDS, true))
                .target(tClass, rootUri); // 这里rootUri在方法内部会被校验，不允许为空字符串或者null
    }

    /**
     * 自定义SSL校验方式，Https请求时会用上，不影响HTTP的请求
     * todo 如果是双向校验，则此处要如何提供证书给服务端校验？
     * @return 自定义SSL校验方式的Client
     */
    public Client ssLClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    System.out.println("(checkClientTrusted)AuthType: " + authType);
                    if (chain == null || chain.length == 0) {
                        System.out.println("未提供证书");
                    } else {
                        for (X509Certificate x509Certificate : chain) {
                            System.out.println("(checkClientTrusted)SubjectDN: " + x509Certificate.getSubjectDN());
                            System.out.println("(checkClientTrusted)IssuerDN: " + x509Certificate.getIssuerDN());
                        }
                    }
                }

                /**
                 * 校验服务端（如果服务端本身是http协议，则不会进入此方法）
                 * @param chain 服务端提供的证书
                 * @param authType 支持的类型
                 * @throws CertificateException 证书校验异常
                 */
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    System.out.println("(checkServerTrusted)AuthType: " + authType);
                    if (chain == null || chain.length == 0) {
                        System.out.println("服务端未提供认证用的证书");
                    } else {
                        for (X509Certificate x509Certificate : chain) {
                            System.out.println("(checkServerTrusted)SubjectDN: " + x509Certificate.getSubjectDN());
                            System.out.println("(checkServerTrusted)IssuerDN: " + x509Certificate.getIssuerDN());
                        }
                        // 这里实际上就可以通过加载上级证书进行对服务端的认证环节
                    }
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

    /**
     * 发送请求前的预处理，例如可以增加请求头之类
     */
    static final class BeforeRequest implements RequestInterceptor {

        private final String str;

        /**
         * 构造函数，可以用来从外界传入一些参数数据
         * @param str 字符串
         */
        BeforeRequest(final String str) {
            this.str = str;
        }

        /**
         * 预处理（这里可以增加请求头，或者对报文进行签名啥的）
         * @param requestTemplate 请求信息
         */
        @Override
        public void apply(RequestTemplate requestTemplate) {
            String timestamp = String.valueOf(new Date().getTime());
            requestTemplate.header("Header-Timestamp", timestamp);
            System.out.println("Request method: " + requestTemplate.method());
            System.out.println("Request path: " + requestTemplate.path());
            System.out.println("Request query line: " + requestTemplate.queryLine());
            if (requestTemplate.body() != null) {
                System.out.println("Request body in hex: " + Hex.toHexString(requestTemplate.body()));
            }
        }
    }
}
