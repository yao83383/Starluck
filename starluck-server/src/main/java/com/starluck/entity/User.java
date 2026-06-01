package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户账号表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 手机号 */
    private String phone;

    /** 密码（BCrypt加密） */
    private String password;

    /** 微信OpenID */
    private String wxOpenid;

    /** 微信UnionID */
    private String wxUnionid;

    /** 邀请码 */
    private String inviteCode;

    /** 邀请人ID */
    private Long inviterId;

    /** 状态：1正常 0禁用 */
    private Integer status;

    /** 角色：USER普通用户 ADMIN管理员 */
    private String role;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

    /** 最后登录IP */
    private String lastLoginIp;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
