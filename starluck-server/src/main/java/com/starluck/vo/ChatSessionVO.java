package com.starluck.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 聊天会话响应
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@Builder
public class ChatSessionVO {

    private Long sessionId;
    private Long peerUserId;
    private String peerName;
    private Integer peerAvatarNo;
    private String peerCity;
    private Boolean peerOnline;
    private Boolean peerVip;
    private Boolean isFake;
    private String lastMsg;
    private String lastTime;
    private Integer unread;
    private String type;
}
