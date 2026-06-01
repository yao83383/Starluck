package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.ChatSendRequest;
import com.starluck.service.ChatService;
import com.starluck.vo.ChatMessageVO;
import com.starluck.vo.ChatSessionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 获取会话列表
     */
    @GetMapping("/sessions")
    public Result<List<ChatSessionVO>> getSessions() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(chatService.getSessionList(userId));
    }

    /**
     * 获取消息历史
     */
    @GetMapping("/messages/{sessionId}")
    public Result<List<ChatMessageVO>> getMessages(@PathVariable Long sessionId) {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(chatService.getMessages(sessionId, userId));
    }

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public Result<ChatMessageVO> sendMessage(@Valid @RequestBody ChatSendRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        ChatMessageVO vo = chatService.sendMessage(userId, request);
        return Result.ok(vo);
    }

    /**
     * 创建或获取会话（用于从用户主页发起聊天）
     */
    @PostMapping("/session/create")
    public Result<ChatSessionVO> createSession(@RequestParam Long peerUserId,
                                                @RequestParam(defaultValue = "false") Boolean isFake) {
        Long userId = SecurityUtil.getCurrentUserId();
        ChatSessionVO vo = chatService.getOrCreateSession(userId, peerUserId, isFake);
        return Result.ok(vo);
    }
}
