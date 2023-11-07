package com.cn.fastcab.config;

import java.util.Map;

/**
 * <p>
 *  响应结果生成工具
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
public class ResultUtil {

    /**
     * 默认成功响应
     * @return ResponseHandler<T>
     */
    public static ResponseHandler genSuccessResult() {
        return new ResponseHandler()
                .setCodeAndMsg(ResponseCodeUtil.SUCCESS);
    }

    /**
     * 带参数的成功默认响应
     * @param data
     * @return ResponseHandler<T>
     */
    public static <T> ResponseHandler<T> genSuccessResult(T data) {
        return new ResponseHandler()
                .setCodeAndMsg(ResponseCodeUtil.SUCCESS)
                .setData(data);

    }

    /**
     * 成功自定义信息带参数响应
     * @param msg
     * @param data
     * @return ResponseHandler<T>
     */
    public static <T> ResponseHandler<T> genSuccessResult(String msg, T data) {
        return new ResponseHandler()
                .setCode(ResponseCodeUtil.SUCCESS.code())
                .setMsg(msg)
                .setData(data);
    }

    /**
     * 成功自定义信息带参数和token响应
     *
     * @param msg
     * @param data
     * @param token
     * @return ResponseHandler<T>
     */
    public static <T> ResponseHandler<T> genSuccessResult(String msg, T data, Map<String, Object> token) {
        return new ResponseHandler()
                .setCode(ResponseCodeUtil.SUCCESS.code())
                .setMsg(msg)
                .setData(data)
                .setToken(token);
    }

    /**
     * 完全自定义成功返回
     * @param code
     * @param msg
     * @param data
     * @param token
     * @return
     * @param <T>
     */
    public static <T> ResponseHandler<T> genSuccessResult(Integer code, String msg, T data, Map<String, Object> token) {
        return new ResponseHandler()
                .setCode(code)
                .setMsg(msg)
                .setData(data)
                .setToken(token);
    }

    /**
     * 失败默认响应
     * @return ResponseHandler
     */
    public static ResponseHandler genFailResult() {
        return new ResponseHandler()
                .setCodeAndMsg(ResponseCodeUtil.FAIL);
    }

    /**
     * 自定义失败默认响应
     * @return ResponseHandler
     */
    public static ResponseHandler genFailResult(Integer code, String msg) {
        return new ResponseHandler()
                .setCode(code)
                .setMsg(msg);
    }

    /**
     * 自定义失败带参响应
     * @param code
     * @param msg
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseHandler<T> genFailResult(Integer code, String msg, T data) {
        return new ResponseHandler()
                .setCode(code)
                .setMsg(msg)
                .setData(data);
    }
}
