package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 聊天消息表
 *
 * @author AI
 * @date 2026-06-01
 */
@TableName("chat_message")
public class ChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sessionId;
    private Long senderId;
    private String senderRole;
    private String msgType;
    private String content;
    private Integer isRead;
    private String giftEmoji;
    private String giftName;
    private Integer costDiamond;
    private String msgTime;
    private Integer replied;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }
    public String getMsgType() { return msgType; }
    public void setMsgType(String msgType) { this.msgType = msgType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public String getGiftEmoji() { return giftEmoji; }
    public void setGiftEmoji(String giftEmoji) { this.giftEmoji = giftEmoji; }
    public String getGiftName() { return giftName; }
    public void setGiftName(String giftName) { this.giftName = giftName; }
    public Integer getCostDiamond() { return costDiamond; }
    public void setCostDiamond(Integer costDiamond) { this.costDiamond = costDiamond; }
    public String getMsgTime() { return msgTime; }
    public void setMsgTime(String msgTime) { this.msgTime = msgTime; }
    public Integer getReplied() { return replied; }
    public void setReplied(Integer replied) { this.replied = replied; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
