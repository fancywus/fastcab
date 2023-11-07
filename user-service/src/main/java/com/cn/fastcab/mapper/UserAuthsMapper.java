package com.cn.fastcab.mapper;

import com.cn.fastcab.entity.UserAuths;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fancy wu
 * @since 2022-12-12
 */
@Mapper
@Repository
public interface UserAuthsMapper extends BaseMapper<UserAuths> {

}
