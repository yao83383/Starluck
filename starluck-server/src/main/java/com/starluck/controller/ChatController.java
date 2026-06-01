package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.ChatSendRequest;
import com.starluck.service.ChatService;
import com.starluck.vo.ChatMessageVO;
import com.starluck.vo.ChatSessionVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/sessions")
    public Result<List<ChatSessionVO>> getSessions() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(chatService.getSessionList(userId));
    }

    @GetMapping("/messages/{sessionId}")
    public Result<List<ChatMessageVO>> getMessages(@PathVariable Long sessionId) {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(chatService.getMessages(sessionId, userId));
    }

    @PostMapping("/send")
    public Result<ChatMessageVO> sendMessage(@Valid @RequestBody ChatSendRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        ChatMessageVO vo = chatService.sendMessage(userId, request);
        return Result.ok(vo);
    }

    @PostMapping("/session/create")
    public Result<ChatSessionVO> createSession(@RequestParam Long peerUserId,
                                                @RequestParam(defaultValue = "false") Boolean isFake) {
        Long userId = SecurityUtil.getCurrentUserId();
        ChatSessionVO vo = chatService.getOrCreateSession(userId, peerUserId, isFake);
        return Result.ok(vo);
    }
}
