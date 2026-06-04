package com.starluck.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starluck.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 将指定会话中某用户发送的消息标记为已读
     */
    @Update("UPDATE chat_message SET is_read = 1 WHERE session_id = #{sessionId} AND sender_id = #{senderId} AND is_read = 0")
    int markAsRead(@Param("sessionId") Long sessionId, @Param("senderId") Long senderId);
}
