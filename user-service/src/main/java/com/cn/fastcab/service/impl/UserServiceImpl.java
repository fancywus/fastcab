package com.cn.fastcab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.fastcab.IPUtil;
import com.cn.fastcab.config.ResponseCodeUtil;
import com.cn.fastcab.config.ResponseHandler;
import com.cn.fastcab.config.ResultUtil;
import com.cn.fastcab.controller.JwtTokenUtil;
import com.cn.fastcab.dto.UserDto;
import com.cn.fastcab.entity.User;
import com.cn.fastcab.mapper.UserMapper;
import com.cn.fastcab.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.fastcab.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户服务业务实现类
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 引入JWT工具类
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class}, timeout = 120, propagation = Propagation.REQUIRED)
    public ResponseHandler register(UserVo user, HttpServletRequest req) {
        try {
            User newUser = new User(user);
            newUser.setRegisterIp(IPUtil.getIpAddr(req));
            userMapper.insert(newUser);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", newUser.getPhone());
            User one = userMapper.selectOne(wrapper);
            UserDto dto = new UserDto(one);
            Map<String, Object> token = jwtTokenUtil.generateToken(dto.getPhone());
            return ResultUtil.genSuccessResult(ResponseCodeUtil.SUCCESS.code(), "注册成功", dto, token);
        } catch (Exception e) {
            throw new RuntimeException("注册异常: " + e);
        }
    }
}
