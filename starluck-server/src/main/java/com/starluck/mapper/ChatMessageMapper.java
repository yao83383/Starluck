package com.starluck.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starluck.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天消息Mapper
 *
 * @author AI
 * @date 2026-06-01
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
}
