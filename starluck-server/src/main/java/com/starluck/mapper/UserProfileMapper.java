package com.starluck.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starluck.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户资料Mapper
 *
 * @author AI
 * @date 2026-06-01
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}
