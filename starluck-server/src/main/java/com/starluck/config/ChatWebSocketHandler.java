package com.starluck.config;

import com.starluck.security.JwtUtil;
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

    /** 用户ID -> WebSocket会话 */
    private static final Map<Long, WebSocketSession> USER_SESSIONS = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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
