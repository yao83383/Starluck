package com.starluck.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.starluck.dto.ChatSendRequest;
import com.starluck.security.JwtUtil;
import com.starluck.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天WebSocket处理器
 *
 * @author AI
 * @date 2026-06-01
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final JwtUtil jwtUtil;
    private final ChatService chatService;

    private static final Map<Long, WebSocketSession> USER_SESSIONS = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(JwtUtil jwtUtil, ChatService chatService) {
        this.jwtUtil = jwtUtil;
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = extractToken(session);
        if (token != null && jwtUtil.validateToken(token)) {
            Long userId = jwtUtil.getUserId(token);
            USER_SESSIONS.put(userId, session);
            session.getAttributes().put("userId", userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) return;
        try {
            JSONObject json = JSONUtil.parseObj(message.getPayload());
            String type = json.getStr("type");
            if ("chat_send".equals(type)) {
                long sessionId = json.getLong("sessionId");
                String content = json.getStr("content");
                if (content == null || content.trim().isEmpty()) return;
                ChatSendRequest req = new ChatSendRequest();
                req.setSessionId(sessionId);
                req.setContent(content.trim());
                chatService.sendMessage(userId, req);
            }
        } catch (Exception e) {
            sendToUser(userId, JSONUtil.toJsonStr(Map.of("type", "error", "msg", e.getMessage())));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            USER_SESSIONS.remove(userId);
        }
    }

    /**
     * 向指定用户推送消息
     */
    public static void sendToUser(Long userId, String message) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                USER_SESSIONS.remove(userId);
            }
        }
    }

    /**
     * 检查用户是否在线
     */
    public static boolean isUserOnline(Long userId) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        return session != null && session.isOpen();
    }

    private String extractToken(WebSocketSession session) {
        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query != null && query.startsWith("token=")) {
            return query.substring(6);
        }
        return null;
    }
}
