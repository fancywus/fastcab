package com.cn.fastcab.entity;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;

import com.cn.fastcab.vo.UserVo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  用户基础信息类
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
@TableName("tbl_user")
@ApiModel(value = "User", description = "用户信息表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //userId
    private Long userId;

    //用户昵称
    private String username;

    //用户密码
    private String password;

    //用户手机号
    private String phone;

    //头像
    private String avatar;

    //性别
    private Integer sex;

    //状态： 0：正常 1：冻结 2：禁用
    private Integer status;

    //注册时间
    private Timestamp registerTime;

    //注册ip
    private String registerIp;

    //登录时间
    private Timestamp loginTime;

    //登录ip
    private String loginIp;

    //登录尝试次数
    private Integer trycount;

    public User(UserVo vo) {
        this.setUserId(IdUtil.getSnowflakeNextId());
        this.setPhone(vo.getPhone());
    }
}
