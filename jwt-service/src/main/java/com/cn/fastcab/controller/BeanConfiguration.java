package com.cn.fastcab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import java.util.Properties;

/**
 * <p>
 *
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/12/10 0010
 * @Version 1.0
 */
@Configuration
@Slf4j
public class BeanConfiguration {
    @Bean
    public YamlConfigurerUtil ymlConfigurerUtil() {
        //1:加载配置文件
        Resource yaml = new ClassPathResource("application-jwt.yml");
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        // 2:将加载的配置文件交给 YamlPropertiesFactoryBean
        yamlPropertiesFactoryBean.setResources(yaml);
        // 3：将yml转换成 key：val
        Properties properties = yamlPropertiesFactoryBean.getObject();
        // 4: 将Properties 通过构造方法交给我们写的工具类
        YamlConfigurerUtil ymlConfigurerUtil = new YamlConfigurerUtil(properties);
        log.info("已执行JWT配置************");
        return ymlConfigurerUtil;
    }
}
