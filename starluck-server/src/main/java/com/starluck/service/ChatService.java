package com.starluck.service;

import com.starluck.dto.ChatSendRequest;
import com.starluck.vo.ChatMessageVO;
import com.starluck.vo.ChatSessionVO;

import java.util.List;

/**
 * 聊天服务接口
 *
 * @author AI
 * @date 2026-06-01
 */
public interface ChatService {

    /**
     * 获取会话列表
     */
    List<ChatSessionVO> getSessionList(Long userId);

    /**
     * 获取消息历史
     */
    List<ChatMessageVO> getMessages(Long sessionId, Long userId);

    /**
     * 发送文字消息（含扣费逻辑）
     */
    ChatMessageVO sendMessage(Long senderId, ChatSendRequest request);

    /**
     * 创建或获取与指定用户的会话
     */
    ChatSessionVO getOrCreateSession(Long maleUserId, Long femaleUserId, boolean isFake);
}
