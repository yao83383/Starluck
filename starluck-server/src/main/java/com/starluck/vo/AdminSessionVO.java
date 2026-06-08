package com.starluck.vo;

public class AdminSessionVO {

    private Long sessionId;
    private Long maleId;
    private String maleName;
    private String malePhone;
    private Integer maleAv;
    private Long femaleId;
    private String femaleName;
    private Integer femaleAv;
    private String lastMsg;
    private String lastTime;
    private Integer maleUnread;
    private Integer femaleUnread;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private AdminSessionVO vo = new AdminSessionVO();
        public Builder sessionId(Long v) { vo.sessionId = v; return this; }
        public Builder maleId(Long v) { vo.maleId = v; return this; }
        public Builder maleName(String v) { vo.maleName = v; return this; }
        public Builder malePhone(String v) { vo.malePhone = v; return this; }
        public Builder maleAv(Integer v) { vo.maleAv = v; return this; }
        public Builder femaleId(Long v) { vo.femaleId = v; return this; }
        public Builder femaleName(String v) { vo.femaleName = v; return this; }
        public Builder femaleAv(Integer v) { vo.femaleAv = v; return this; }
        public Builder lastMsg(String v) { vo.lastMsg = v; return this; }
        public Builder lastTime(String v) { vo.lastTime = v; return this; }
        public Builder maleUnread(Integer v) { vo.maleUnread = v; return this; }
        public Builder femaleUnread(Integer v) { vo.femaleUnread = v; return this; }
        public AdminSessionVO build() { return vo; }
    }

    public Long getSessionId() { return sessionId; }
    public Long getMaleId() { return maleId; }
    public String getMaleName() { return maleName; }
    public String getMalePhone() { return malePhone; }
    public Integer getMaleAv() { return maleAv; }
    public Long getFemaleId() { return femaleId; }
    public String getFemaleName() { return femaleName; }
    public Integer getFemaleAv() { return femaleAv; }
    public String getLastMsg() { return lastMsg; }
    public String getLastTime() { return lastTime; }
    public Integer getMaleUnread() { return maleUnread; }
    public Integer getFemaleUnread() { return femaleUnread; }
}
