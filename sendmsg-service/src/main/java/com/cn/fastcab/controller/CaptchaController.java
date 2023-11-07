package com.cn.fastcab.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  验证码生成和校验、刷新验证码功能
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/12/10 0010
 * @Version 1.0
 */
@RestController
@RequestMapping("captcha")
public class CaptchaController {

    public static final String CAPTCHA_CODE_NAME = "fastcab:sendmsg:";

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取、刷新验证码
     * @throws IOException
     */
    @GetMapping("/getCaptcha")
    public void getCaptchaCode() throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        //获取验证码
        String code = captcha.getCode();
        if (code != "" && code != null && code.length() == 4) {
            //放入redis进行缓存，60秒过期
            stringRedisTemplate.opsForValue().set(CAPTCHA_CODE_NAME + code, code, 60, TimeUnit.SECONDS);
            System.out.println(code);
            //图形验证码写出，可以写出到文件，也可以写出到流
            captcha.write(response.getOutputStream());
        }
        else {
            System.err.println("获取验证码错误！");
            throw new RuntimeException();
        }

    }

    /**
     * 校验验证码
     * @param code 四位长度验证码
     * @return true/false
     * @throws IOException
     */
    @GetMapping("/verifyCode")
    public boolean verifyCode(@RequestParam("code") String code) throws IOException {
        //判断验证码
        if (code != "" && code != null && code.length() == 4 ) {
            //从redis获取验证码
            Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(CAPTCHA_CODE_NAME + code);
            //判断key有没有过期
            if (expire != -2) {
                String verifyCode = (String) stringRedisTemplate.opsForValue().get(CAPTCHA_CODE_NAME + code);
                //如果验证码不相等
                if (!verifyCode.equals(code)) {
                    System.err.println("验证码不相等");
                    return false;
                }
                return true;
            }
            System.err.println("验证码已过期，请重新刷新！");
            return false;
        }
        return false;
    }

}
