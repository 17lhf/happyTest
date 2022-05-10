package com.basic.happytest.modules.uniformPackagingReturn.resp;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回数据统一封装
 * @author lhf
 */

@Getter
@Setter
public class Result<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 信息说明
     */
    private String msg;

    /**
     * 应答数据
     */
    private T data;

    private static<T> Result<T> baseCreate(int code, String msg, T data){
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static<T> Result<T> success(T data) {
        return baseCreate(0, "OK", data);
    }

    public static<T> Result<T> error(int code, String msg) {
        return baseCreate(code, msg, null);
    }

}
