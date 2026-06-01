package com.starluck.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户资料响应
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@Builder
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
}
