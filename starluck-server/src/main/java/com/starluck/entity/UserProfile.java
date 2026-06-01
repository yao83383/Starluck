package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 用户资料表
 *
 * @author AI
 * @date 2026-06-01
 */
@TableName("user_profile")
public class UserProfile {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer avatarNo;
    private String gender;
    private Integer age;
    private String birthday;
    private String city;
    private Integer height;
    private Integer weight;
    private String job;
    private String edu;
    private String hometown;
    private String loveStatus;
    private String sign;
    private String tags;
    private Integer fansCount;
    private Integer followCount;
    private Integer friendsCount;
    private Integer likesCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Integer getAvatarNo() { return avatarNo; }
    public void setAvatarNo(Integer avatarNo) { this.avatarNo = avatarNo; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
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
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Integer getFansCount() { return fansCount; }
    public void setFansCount(Integer fansCount) { this.fansCount = fansCount; }
    public Integer getFollowCount() { return followCount; }
    public void setFollowCount(Integer followCount) { this.followCount = followCount; }
    public Integer getFriendsCount() { return friendsCount; }
    public void setFriendsCount(Integer friendsCount) { this.friendsCount = friendsCount; }
    public Integer getLikesCount() { return likesCount; }
    public void setLikesCount(Integer likesCount) { this.likesCount = likesCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
