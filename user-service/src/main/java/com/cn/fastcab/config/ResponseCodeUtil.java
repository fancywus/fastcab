package com.cn.fastcab.config;

/**
 * <p>
 *  响应码枚举类
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
public enum ResponseCodeUtil {

    SUCCESS(20000, "成功"),
    FAIL(40004, "错误"),
    UN_USER(21001, "该用户不存在"),
    LOGIN_ARG_FAIL(21002, "用户密码输入不正确"),
    ACCOUNT_FAIL(21003, "对不起，您的账号已经被冻结，请联系客服解决，谢谢"),
    UNLAWFUL_ARG_VALUE(21004, "非法参数错误");

    private final Integer code;

    private final String msg;

    ResponseCodeUtil(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public String msg() {
        return msg;
    }

}
