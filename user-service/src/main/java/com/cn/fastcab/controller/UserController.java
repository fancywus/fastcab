package com.cn.fastcab.controller;

import cn.hutool.json.JSONUtil;
import com.cn.fastcab.config.ResponseCodeUtil;
import com.cn.fastcab.config.ResponseHandler;
import com.cn.fastcab.config.ResultUtil;
import com.cn.fastcab.constant.IdentityTypeValue;
import com.cn.fastcab.dto.UserDto;
import com.cn.fastcab.entity.User;
import com.cn.fastcab.entity.UserAuths;
import com.cn.fastcab.service.IUserAuthsService;
import com.cn.fastcab.service.IUserService;
import com.cn.fastcab.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 *  用户服务相关接口
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserAuthsService userAuthsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 引入JWT工具类
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 登录业务
     * @param
     * @return 响应处理
     */
    @PostMapping("/login")
    public ResponseHandler login(@RequestParam("identifier") String identifier,
                                 @RequestParam("credential") String credential,
                                    @RequestParam("identityType") String identityType) throws JsonProcessingException {
        //如果通过手机登录方式进入
        if (IdentityTypeValue.LOGIN_TYPE_PHONE.equals(identityType)) {
            //根据标识查询是否存在该用户
            UserAuths userAuths = userAuthsService.query().eq("identifier", identifier).one();
            User user = userService.query().eq("userId", userAuths.getUserId()).one();
            if (userAuths == null) {
                return ResultUtil.genFailResult(ResponseCodeUtil.UN_USER.code(), ResponseCodeUtil.UN_USER.msg());
            }
            //判断登录状态是否正常
            if (user.getStatus() == 0) {
                //判断密码是否正确
                boolean matches = passwordEncoder.matches(credential, userAuths.getCredential());
                if (!matches) {
                    return ResultUtil.genFailResult(ResponseCodeUtil.LOGIN_ARG_FAIL.code(), ResponseCodeUtil.LOGIN_ARG_FAIL.msg());
                }
                UserDto userDto = new UserDto(user);
                Map<String, Object> token = jwtTokenUtil.generateToken(userDto.getPhone());
                return ResultUtil.genSuccessResult(ResponseCodeUtil.SUCCESS.code(), "登录成功", JSONUtil.parseObj(userDto), token);
            }
            else
            {
                return ResultUtil.genFailResult(ResponseCodeUtil.ACCOUNT_FAIL.code(), ResponseCodeUtil.ACCOUNT_FAIL.msg());
            }
        }
        //如果是第三方登录
//        else if (IdentityTypeValue.LOGIN_TYPE_QQ.equals(identityType)) {
//            thirdLogin();
//        }
        return null;
    }

//    @GetMapping()
//    public void thirdLogin() {
//    }

    /**
     * 用户注册接口
     *
     * @param user 用户注册实体类
     * @param req request
     * @return ResponseHandler
     */
    @PostMapping("/register")
    public ResponseHandler register(@RequestBody @Validated UserVo user, HttpServletRequest req) {
        ResponseHandler register = userService.register(user, req);
        return register;
    }
}
