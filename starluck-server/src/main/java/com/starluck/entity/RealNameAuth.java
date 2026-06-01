package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实名认证表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("real_name_auth")
public class RealNameAuth {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 真实姓名 */
    private String realName;

    /** 身份证号（AES加密存储） */
    private String idCard;

    /** 身份证正面照URL */
    private String idCardFront;

    /** 身份证反面照URL */
    private String idCardBack;

    /** 人脸照片URL */
    private String faceImage;

    /** 出生日期 */
    private String birthday;

    /** 审核状态：pending待审核 approved通过 rejected拒绝 */
    private String status;

    /** 审核备注 */
    private String auditRemark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
