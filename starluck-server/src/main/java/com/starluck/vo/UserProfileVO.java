package com.starluck.vo;

import java.math.BigDecimal;
import java.util.List;

public class UserProfileVO {

    private Long userId;
    private String nickname;
    private String avatar;
    private Integer avatarNo;
    private String phone;
    private String gender;
    private Integer age;
    private String city;
    private Integer height;
    private Integer weight;
    private String job;
    private String edu;
    private String hometown;
    private String loveStatus;
    private String sign;
    private List<String> tags;
    private String inviteCode;
    private Integer diamonds;
    private BigDecimal cash;
    private Boolean isVip;
    private String vipExpireTime;
    private Boolean isAuthed;
    private Integer fansCount;
    private Integer followCount;
    private Integer friendsCount;
    private Integer likesCount;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UserProfileVO vo = new UserProfileVO();
        public Builder userId(Long v) { vo.userId = v; return this; }
        public Builder nickname(String v) { vo.nickname = v; return this; }
        public Builder avatar(String v) { vo.avatar = v; return this; }
        public Builder avatarNo(Integer v) { vo.avatarNo = v; return this; }
        public Builder phone(String v) { vo.phone = v; return this; }
        public Builder gender(String v) { vo.gender = v; return this; }
        public Builder age(Integer v) { vo.age = v; return this; }
        public Builder city(String v) { vo.city = v; return this; }
        public Builder height(Integer v) { vo.height = v; return this; }
        public Builder weight(Integer v) { vo.weight = v; return this; }
        public Builder job(String v) { vo.job = v; return this; }
        public Builder edu(String v) { vo.edu = v; return this; }
        public Builder hometown(String v) { vo.hometown = v; return this; }
        public Builder loveStatus(String v) { vo.loveStatus = v; return this; }
        public Builder sign(String v) { vo.sign = v; return this; }
        public Builder tags(List<String> v) { vo.tags = v; return this; }
        public Builder inviteCode(String v) { vo.inviteCode = v; return this; }
        public Builder diamonds(Integer v) { vo.diamonds = v; return this; }
        public Builder cash(BigDecimal v) { vo.cash = v; return this; }
        public Builder isVip(Boolean v) { vo.isVip = v; return this; }
        public Builder vipExpireTime(String v) { vo.vipExpireTime = v; return this; }
        public Builder isAuthed(Boolean v) { vo.isAuthed = v; return this; }
        public Builder fansCount(Integer v) { vo.fansCount = v; return this; }
        public Builder followCount(Integer v) { vo.followCount = v; return this; }
        public Builder friendsCount(Integer v) { vo.friendsCount = v; return this; }
        public Builder likesCount(Integer v) { vo.likesCount = v; return this; }
        public UserProfileVO build() { return vo; }
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Integer getAvatarNo() { return avatarNo; }
    public void setAvatarNo(Integer avatarNo) { this.avatarNo = avatarNo; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    public String getEdu() { return edu; }
    public void setEdu(String edu) { this.edu = edu; }
    public String getHometown() { return hometown; }
    public void setHometown(String hometown) { this.hometown = hometown; }
    public String getLoveStatus() { return loveStatus; }
    public void setLoveStatus(String loveStatus) { this.loveStatus = loveStatus; }
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getInviteCode() { return inviteCode; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
    public Integer getDiamonds() { return diamonds; }
    public void setDiamonds(Integer diamonds) { this.diamonds = diamonds; }
    public BigDecimal getCash() { return cash; }
    public void setCash(BigDecimal cash) { this.cash = cash; }
    public Boolean getIsVip() { return isVip; }
    public void setIsVip(Boolean isVip) { this.isVip = isVip; }
    public String getVipExpireTime() { return vipExpireTime; }
    public void setVipExpireTime(String vipExpireTime) { this.vipExpireTime = vipExpireTime; }
    public Boolean getIsAuthed() { return isAuthed; }
    public void setIsAuthed(Boolean isAuthed) { this.isAuthed = isAuthed; }
    public Integer getFansCount() { return fansCount; }
    public void setFansCount(Integer fansCount) { this.fansCount = fansCount; }
    public Integer getFollowCount() { return followCount; }
    public void setFollowCount(Integer followCount) { this.followCount = followCount; }
    public Integer getFriendsCount() { return friendsCount; }
    public void setFriendsCount(Integer friendsCount) { this.friendsCount = friendsCount; }
    public Integer getLikesCount() { return likesCount; }
    public void setLikesCount(Integer likesCount) { this.likesCount = likesCount; }
}
