package com.cn.fastcab.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Jwt属性配置
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/12/1 0001
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "fastcab.jwt")
@Component
public class JwtProperties{

    //是否开启JWT登录认证功能
    private boolean enabled;

    //JWT 令牌的有效期，用于校验JWT令牌的合法性，一个小时,单位毫秒
    private long expiration;
}
