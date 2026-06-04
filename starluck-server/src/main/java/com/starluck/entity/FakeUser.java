package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 假用户（运营用女性账号）
 * 与 user 表结构对齐，独立存储，物理隔离
 *
 * @author AI
 * @date 2026-06-01
 * @ai-assisted ai辅助生成,开发人员已完成审查与测试。
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

    // ==== 新字段：对齐 user 表，和真用户一样的数据 ====
    private String phone;
    private String gender;
    private Integer isAuthed;
    private Integer diamonds;
    private BigDecimal cash;
    private String userType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // ==== 原有 getter/setter ====
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

    // ==== 新字段 getter/setter ====
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getIsAuthed() { return isAuthed; }
    public void setIsAuthed(Integer isAuthed) { this.isAuthed = isAuthed; }
    public Integer getDiamonds() { return diamonds; }
    public void setDiamonds(Integer diamonds) { this.diamonds = diamonds; }
    public BigDecimal getCash() { return cash; }
    public void setCash(BigDecimal cash) { this.cash = cash; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
