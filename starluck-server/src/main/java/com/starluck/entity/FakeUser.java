package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 假用户（运营用女性账号）
 *
 * @author AI
 * @date 2026-06-01
 */
@TableName("fake_user")
public class FakeUser {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer avatarNo;
    private Integer age;
    private String city;
    private String dist;
    private String tags;
    private String sign;
    private Boolean online;
    private Boolean vip;
    private Integer height;
    private Integer weight;
    private String job;
    private String edu;
    private String hometown;
    private String loveStatus;
    private String persona;
    private String csOwner;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAvatarNo() { return avatarNo; }
    public void setAvatarNo(Integer avatarNo) { this.avatarNo = avatarNo; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDist() { return dist; }
    public void setDist(String dist) { this.dist = dist; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    public Boolean getOnline() { return online; }
    public void setOnline(Boolean online) { this.online = online; }
    public Boolean getVip() { return vip; }
    public void setVip(Boolean vip) { this.vip = vip; }
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
    public String getPersona() { return persona; }
    public void setPersona(String persona) { this.persona = persona; }
    public String getCsOwner() { return csOwner; }
    public void setCsOwner(String csOwner) { this.csOwner = csOwner; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
