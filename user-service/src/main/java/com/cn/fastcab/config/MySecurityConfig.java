package com.cn.fastcab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 *  自定义Security Config项
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/29 0029
 * @Version 1.0
 */
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    //使用默认的BCryptPasswordEncoder加密方案
    @Bean
    public PasswordEncoder passwordEncoder() {

        //strength=10，即密钥的迭代次数(strength取值在4~31之间，默认为10)
        return new BCryptPasswordEncoder();
    }

    /**
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/login","/user/register")
                .permitAll()
                .and()
                .csrf()
                .disable();
    }
}
