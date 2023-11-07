package com.cn.fastcab.mapper;

import com.cn.fastcab.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/28
 * @Version 1.0
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}
