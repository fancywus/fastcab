package com.cn.fastcab.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 *  用户注册填写信息实体类
 * </p>
 *
 * @Author 伍繁长
 * @Date 2023/4/5 0005
 * @Version 1.0
 */
@ApiModel(value = "UserVo", description = "用户注册实体类")
@Data
public class UserVo {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "[1]([3-9])[0-9]{9}$", message = "请输入合法的手机号")
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;
}
