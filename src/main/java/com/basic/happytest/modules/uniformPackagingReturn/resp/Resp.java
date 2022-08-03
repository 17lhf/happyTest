package com.basic.happytest.modules.uniformPackagingReturn.resp;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 应答统一封装处理类 <br/>
 * @ ControllerAdvice是一个非常有用的注解，它的作用是增强Controller的扩展功能类<br/>
 * 1.对Controller全局数据统一处理，例如，这里就是对response统一封装<br/>
 * 2.对Controller全局异常统一处理<br/>
 * basePackages不加的话会对某些特殊功能产生冲突，例如 不加的话，在使用swagger时会出现空白页异常<br/>
 * 这里是注明要扫描的包<br/>
 * @author lhf
*/
@ControllerAdvice(basePackages = "com.basic.happytest.modules.uniformPackagingReturn.controller")
public class Resp implements ResponseBodyAdvice<Object> {
    /**
     * 是否支持advice功能，即是否要处理返回的结果
     * true=支持，false=不支持
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 处理response的具体业务方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result){
            return body;
        } else if(body instanceof String){
            Result<Object> result = Result.success(body);
            return JSON.toJSONString(result);
        }
        return Result.success(body);
    }
}
