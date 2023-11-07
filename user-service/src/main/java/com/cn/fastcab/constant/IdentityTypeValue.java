package com.cn.fastcab.constant;

import lombok.Data;

/**
 * <p>
 *  登录类型 (手机号:Phone) 或第三方应用名称 (微信:Wechat , 微博:Weibo, QQ等)
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/12/12 0012
 * @Version 1.0
 */
@Data
public class IdentityTypeValue {

    public static final String LOGIN_TYPE_PHONE = "Phone";

    public static final String LOGIN_TYPE_WECHAT = "Wechat";

    public static final String LOGIN_TYPE_WEIBO = "Weibo";

    public static final String LOGIN_TYPE_QQ = "QQ";
}
