package com.starluck.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starluck.entity.FakeUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 假用户Mapper
 *
 * @author AI
 * @date 2026-06-01
 */
@Mapper
public interface FakeUserMapper extends BaseMapper<FakeUser> {
}
