package com.cn.fastcab.service;

import com.cn.fastcab.config.ResponseHandler;
import com.cn.fastcab.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.fastcab.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  用户服务业务层
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
public interface IUserService extends IService<User> {

    ResponseHandler register(UserVo user, HttpServletRequest req);
}
