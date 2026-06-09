package com.starluck.vo;

import java.util.List;

public class MaleUserVO {

    private Long userId;
    private String phone;
    private String nickname;
    private Integer avatarNo;
    private Integer age;
    private String city;
    private Integer diamonds;
    private String lastLoginTime;
    private Integer sessionCount;
    private List<ActiveSession> activeSessions;

    public static class ActiveSession {
        public Long sessionId;
        public Long fakeId;
        public String fakeName;
        public Integer fakeAv;
        public String lastMsg;
        public String lastTime;
        public Integer femaleUnread;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private MaleUserVO vo = new MaleUserVO();
        public Builder userId(Long v) { vo.userId = v; return this; }
        public Builder phone(String v) { vo.phone = v; return this; }
        public Builder nickname(String v) { vo.nickname = v; return this; }
        public Builder avatarNo(Integer v) { vo.avatarNo = v; return this; }
        public Builder age(Integer v) { vo.age = v; return this; }
        public Builder city(String v) { vo.city = v; return this; }
        public Builder diamonds(Integer v) { vo.diamonds = v; return this; }
        public Builder lastLoginTime(String v) { vo.lastLoginTime = v; return this; }
        public Builder sessionCount(Integer v) { vo.sessionCount = v; return this; }
        public Builder activeSessions(List<ActiveSession> v) { vo.activeSessions = v; return this; }
        public MaleUserVO build() { return vo; }
    }

    public Long getUserId() { return userId; }
    public String getPhone() { return phone; }
    public String getNickname() { return nickname; }
    public Integer getAvatarNo() { return avatarNo; }
    public Integer getAge() { return age; }
    public String getCity() { return city; }
    public Integer getDiamonds() { return diamonds; }
    public String getLastLoginTime() { return lastLoginTime; }
    public Integer getSessionCount() { return sessionCount; }
    public List<ActiveSession> getActiveSessions() { return activeSessions; }
}
