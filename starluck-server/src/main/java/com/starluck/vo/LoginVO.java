package com.starluck.vo;

/**
 * 登录响应
 *
 * @author AI
 * @date 2026-06-01
 */
public class LoginVO {

    private String token;
    private Long userId;
    private String phone;
    private String nickname;
    private String avatar;
    private Boolean onboarded;
    private Integer diamonds;
    private Boolean isVip;
    private String vipExpireTime;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private LoginVO vo = new LoginVO();
        public Builder token(String v) { vo.token = v; return this; }
        public Builder userId(Long v) { vo.userId = v; return this; }
        public Builder phone(String v) { vo.phone = v; return this; }
        public Builder nickname(String v) { vo.nickname = v; return this; }
        public Builder avatar(String v) { vo.avatar = v; return this; }
        public Builder onboarded(Boolean v) { vo.onboarded = v; return this; }
        public Builder diamonds(Integer v) { vo.diamonds = v; return this; }
        public Builder isVip(Boolean v) { vo.isVip = v; return this; }
        public Builder vipExpireTime(String v) { vo.vipExpireTime = v; return this; }
        public LoginVO build() { return vo; }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Boolean getOnboarded() { return onboarded; }
    public void setOnboarded(Boolean onboarded) { this.onboarded = onboarded; }
    public Integer getDiamonds() { return diamonds; }
    public void setDiamonds(Integer diamonds) { this.diamonds = diamonds; }
    public Boolean getIsVip() { return isVip; }
    public void setIsVip(Boolean isVip) { this.isVip = isVip; }
    public String getVipExpireTime() { return vipExpireTime; }
    public void setVipExpireTime(String vipExpireTime) { this.vipExpireTime = vipExpireTime; }
}
