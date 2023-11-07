package com.cn.fastcab.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * <p>
 * 对于controller响应结果统一做处理返回给前端
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
@ApiModel(value = "Result", description = "统一返回结果")
@Data
public class ResponseHandler<T> {

    @ApiModelProperty(value = "响应状态码")
    private Integer code;

    @ApiModelProperty(value = "响应状态码信息")
    private String msg;

    @ApiModelProperty(value = "响应体参数,json格式")
    private T data;

    @ApiModelProperty(value = "token信息")
    private Map<String, Object> token;

    public ResponseHandler<T> setCodeAndMsg(ResponseCodeUtil responseCodeUtil) {
        this.code = responseCodeUtil.code();
        this.msg = responseCodeUtil.msg();
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public ResponseHandler<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseHandler<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseHandler<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getToken() {
        return token;
    }

    public ResponseHandler<T> setToken(Map<String, Object> token) {
        this.token = token;
        return this;
    }
}
