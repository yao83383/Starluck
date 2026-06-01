package com.starluck.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starluck.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author AI
 * @date 2026-06-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
