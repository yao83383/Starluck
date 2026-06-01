package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 礼物目录表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("gift")
public class Gift {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 礼物名称 */
    private String name;

    /** 礼物emoji图标 */
    private String icon;

    /** 分类：hot热门 romantic浪漫 luxury豪华 */
    private String category;

    /** 价格（钻石） */
    private Integer price;

    /** 排序 */
    private Integer sort;

    /** 状态：1上架 0下架 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
