package com.starluck.vo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.starluck.entity.FakeUser;

import java.util.Collections;
import java.util.List;

/**
 * 发现页用户卡片 VO（轻量版）
 *
 * @author AI
 * @date 2026-06-03
 * @ai-assisted ai辅助生成,开发人员已完成审查与测试。
 */
public class UserCardVO {

    private Long userId;
    private String nickname;
    private Integer avatarNo;
    private String gender;
    private Integer age;
    private String city;
    private String dist;
    private String sign;
    private List<String> tags;
    private Boolean isVip;
    private Boolean online;
    private Boolean isFake;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public Integer getAvatarNo() { return avatarNo; }
    public void setAvatarNo(Integer avatarNo) { this.avatarNo = avatarNo; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDist() { return dist; }
    public void setDist(String dist) { this.dist = dist; }
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public Boolean getIsVip() { return isVip; }
    public void setIsVip(Boolean isVip) { this.isVip = isVip; }
    public Boolean getOnline() { return online; }
    public void setOnline(Boolean online) { this.online = online; }
    public Boolean getIsFake() { return isFake; }
    public void setIsFake(Boolean isFake) { this.isFake = isFake; }

    public static UserCardVO build(Long userId, String nickname, Integer avatarNo, String gender,
                                    Integer age, String city, String sign, List<String> tags) {
        UserCardVO vo = new UserCardVO();
        vo.userId = userId;
        vo.nickname = nickname;
        vo.avatarNo = avatarNo != null ? avatarNo : 1;
        vo.gender = gender;
        vo.age = age != null ? age : 20;
        vo.city = city != null ? city : "未设置";
        vo.sign = sign != null ? sign : "";
        vo.tags = tags != null ? tags : java.util.Collections.emptyList();
        vo.isVip = false;
        vo.online = true;
        vo.isFake = false;
        return vo;
    }

    /** 从假用户构建 */
    public static UserCardVO fromFake(FakeUser user) {
        UserCardVO vo = new UserCardVO();
        vo.userId = user.getId();
        vo.nickname = StrUtil.isNotBlank(user.getName()) ? user.getName() : "神秘星人";
        vo.avatarNo = user.getAvatarNo() != null ? user.getAvatarNo() : 1;
        vo.gender = user.getGender() != null ? user.getGender() : "F";
        vo.age = user.getAge() != null ? user.getAge() : 22;
        vo.city = StrUtil.isNotBlank(user.getCity()) ? user.getCity() : "未设置";
        vo.sign = StrUtil.isNotBlank(user.getSign()) ? user.getSign() : "";
        vo.isVip = user.getVip() != null ? user.getVip() : false;
        vo.online = user.getOnline() != null ? user.getOnline() : true;
        vo.isFake = true;

        List<String> tags = Collections.emptyList();
        if (StrUtil.isNotBlank(user.getTags())) {
            try { tags = JSONUtil.toList(user.getTags(), String.class); } catch (Exception e) {}
        }
        vo.tags = tags;
        return vo;
    }
}
