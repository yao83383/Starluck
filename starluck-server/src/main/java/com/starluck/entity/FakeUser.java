package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 假用户（运营用女性账号）
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("fake_user")
public class FakeUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 昵称 */
    private String name;

    /** 头像编号（1-8） */
    private Integer avatarNo;

    /** 年龄 */
    private Integer age;

    /** 城市 */
    private String city;

    /** 距离描述（如"1.2km"） */
    private String dist;

    /** 兴趣标签（JSON数组） */
    private String tags;

    /** 个性签名 */
    private String sign;

    /** 是否在线 */
    private Boolean online;

    /** 是否VIP */
    private Boolean vip;

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

    /** 情感状态 */
    private String loveStatus;

    /** 人设描述（给AI的角色背景） */
    private String persona;

    /** 客服认领人 */
    private String csOwner;

    /** 状态：1启用 0停用 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
