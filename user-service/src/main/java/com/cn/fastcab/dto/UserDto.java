package com.cn.fastcab.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cn.fastcab.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 *  返回给前端的User类
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/29 0029
 * @Version 1.0
 */
@ApiModel(value = "UserDto", description = "用户信息Dto,用于前端展示")
@Data
@NoArgsConstructor
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //userId
    private Long userId;

    //用户昵称
    private String username;

    //用户手机号
    private String phone;

    //头像
    private String avatar;

    //性别
    private Integer sex;

    //注册时间
    private Timestamp registerTime;

    //注册ip
    private String registerIp;

    //登录时间
    private Timestamp loginTime;

    //登录ip
    private String loginIp;

    public UserDto(User user) {
       this.setUserId(user.getUserId());
       this.setUsername(user.getUsername());
       this.setPhone(user.getPhone());
       this.setAvatar(user.getAvatar());
       this.setSex(user.getSex());
       this.setRegisterTime(user.getRegisterTime());
       this.setRegisterIp(user.getRegisterIp());
       this.setLoginTime(user.getLoginTime());
       this.setLoginIp(user.getLoginIp());
    }
}
