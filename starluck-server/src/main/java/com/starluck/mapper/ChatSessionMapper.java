package com.starluck.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starluck.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天会话Mapper
 *
 * @author AI
 * @date 2026-06-01
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
}
