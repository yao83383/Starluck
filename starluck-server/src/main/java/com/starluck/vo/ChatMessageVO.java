package com.starluck.vo;

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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private ChatMessageVO vo = new ChatMessageVO();
        public Builder id(Long v) { vo.id = v; return this; }
        public Builder sessionId(Long v) { vo.sessionId = v; return this; }
        public Builder senderId(Long v) { vo.senderId = v; return this; }
        public Builder senderRole(String v) { vo.senderRole = v; return this; }
        public Builder msgType(String v) { vo.msgType = v; return this; }
        public Builder content(String v) { vo.content = v; return this; }
        public Builder giftEmoji(String v) { vo.giftEmoji = v; return this; }
        public Builder giftName(String v) { vo.giftName = v; return this; }
        public Builder costDiamond(Integer v) { vo.costDiamond = v; return this; }
        public Builder msgTime(String v) { vo.msgTime = v; return this; }
        public ChatMessageVO build() { return vo; }
    }

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
    public String getGiftEmoji() { return giftEmoji; }
    public void setGiftEmoji(String giftEmoji) { this.giftEmoji = giftEmoji; }
    public String getGiftName() { return giftName; }
    public void setGiftName(String giftName) { this.giftName = giftName; }
    public Integer getCostDiamond() { return costDiamond; }
    public void setCostDiamond(Integer costDiamond) { this.costDiamond = costDiamond; }
    public String getMsgTime() { return msgTime; }
    public void setMsgTime(String msgTime) { this.msgTime = msgTime; }
}
