package com.cn.fastcab.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author fancy wu
 * @since 2022-12-12
 */
@TableName("tbl_user_auths")
@ApiModel(value = "UserAuths对象", description = "用户登录记录和密码的表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuths implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //uuid userid
    private Long userId;

    //登录类型 (手机号:Phone) 或第三方应用名称 (微信:Wechat , 微博:Weibo, QQ等)
    private String identityType;

    //标识 (手机号或第三方应用的唯一标识)
    private String identifier;

    //密码凭证 (站内的保存密码 , 站外的不保存或保存token)
    private String credential;

    //注册时间
    private Data registerTime;

    //注册ip
    private String registerIp;

    //登录尝试次数 3次当天无法登录 联系客服
    private Integer trycount;
}
