package com.starluck.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 聊天消息响应
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@Builder
public class ChatMessageVO {

    private Long id;
    private Long sessionId;
    private Long senderId;
    private String senderRole;
    private String msgType;
    private String content;
    private String giftEmoji;
    private String giftName;
    private Integer costDiamond;
    private String msgTime;
}
