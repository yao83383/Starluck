package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户资料表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("user_profile")
public class UserProfile {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** 头像编号（1-8，默认渐变色头像） */
    private Integer avatarNo;

    /** 性别：M男 F女 */
    private String gender;

    /** 年龄 */
    private Integer age;

    /** 生日 */
    private String birthday;

    /** 城市 */
    private String city;

    /** 身高(cm) */
    private Integer height;

    /** 体重(kg) */
    private Integer weight;

    /** 职业 */
    private String job;

    /** 学历 */
    private String edu;

    /** 家乡 */
    private String hometown;

    /** 情感状态：单身/恋爱中/保密 */
    private String loveStatus;

    /** 个性签名 */
    private String sign;

    /** 兴趣标签（JSON数组） */
    private String tags;

    /** 粉丝数 */
    private Integer fansCount;

    /** 关注数 */
    private Integer followCount;

    /** 好友数 */
    private Integer friendsCount;

    /** 获赞数 */
    private Integer likesCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
