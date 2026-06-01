package com.starluck.vo;

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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private ChatSessionVO vo = new ChatSessionVO();
        public Builder sessionId(Long v) { vo.sessionId = v; return this; }
        public Builder peerUserId(Long v) { vo.peerUserId = v; return this; }
        public Builder peerName(String v) { vo.peerName = v; return this; }
        public Builder peerAvatarNo(Integer v) { vo.peerAvatarNo = v; return this; }
        public Builder peerCity(String v) { vo.peerCity = v; return this; }
        public Builder peerOnline(Boolean v) { vo.peerOnline = v; return this; }
        public Builder peerVip(Boolean v) { vo.peerVip = v; return this; }
        public Builder isFake(Boolean v) { vo.isFake = v; return this; }
        public Builder lastMsg(String v) { vo.lastMsg = v; return this; }
        public Builder lastTime(String v) { vo.lastTime = v; return this; }
        public Builder unread(Integer v) { vo.unread = v; return this; }
        public Builder type(String v) { vo.type = v; return this; }
        public ChatSessionVO build() { return vo; }
    }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    public Long getPeerUserId() { return peerUserId; }
    public void setPeerUserId(Long peerUserId) { this.peerUserId = peerUserId; }
    public String getPeerName() { return peerName; }
    public void setPeerName(String peerName) { this.peerName = peerName; }
    public Integer getPeerAvatarNo() { return peerAvatarNo; }
    public void setPeerAvatarNo(Integer peerAvatarNo) { this.peerAvatarNo = peerAvatarNo; }
    public String getPeerCity() { return peerCity; }
    public void setPeerCity(String peerCity) { this.peerCity = peerCity; }
    public Boolean getPeerOnline() { return peerOnline; }
    public void setPeerOnline(Boolean peerOnline) { this.peerOnline = peerOnline; }
    public Boolean getPeerVip() { return peerVip; }
    public void setPeerVip(Boolean peerVip) { this.peerVip = peerVip; }
    public Boolean getIsFake() { return isFake; }
    public void setIsFake(Boolean isFake) { this.isFake = isFake; }
    public String getLastMsg() { return lastMsg; }
    public void setLastMsg(String lastMsg) { this.lastMsg = lastMsg; }
    public String getLastTime() { return lastTime; }
    public void setLastTime(String lastTime) { this.lastTime = lastTime; }
    public Integer getUnread() { return unread; }
    public void setUnread(Integer unread) { this.unread = unread; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
