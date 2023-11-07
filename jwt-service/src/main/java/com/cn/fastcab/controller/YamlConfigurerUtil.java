package com.cn.fastcab.controller;

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
public class YamlConfigurerUtil {
    private static Properties ymlProperties = new Properties();

    public YamlConfigurerUtil(Properties properties){
        ymlProperties = properties;
    }

    public static String getStrYmlVal(String key){
        System.out.println("已执行----------------");
        return ymlProperties.getProperty(key);
    }

    public static Integer getIntegerYmlVal(String key){
        return Integer.valueOf(ymlProperties.getProperty(key));
    }
}
