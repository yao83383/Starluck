-- =============================================
-- 一青友人 · Starluck 数据库初始化脚本
-- 版本：v1.0
-- 日期：2026-06-01
-- =============================================

CREATE DATABASE IF NOT EXISTS starluck DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE starluck;

-- ----------------------------
-- 1. 用户账号表
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `phone`           VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `password`        VARCHAR(200) DEFAULT NULL COMMENT '密码（BCrypt加密）',
    `wx_openid`       VARCHAR(100) DEFAULT NULL COMMENT '微信OpenID',
    `wx_unionid`      VARCHAR(100) DEFAULT NULL COMMENT '微信UnionID',
    `invite_code`     VARCHAR(20)  DEFAULT NULL COMMENT '我的邀请码',
    `inviter_id`      BIGINT       DEFAULT NULL COMMENT '邀请人ID',
    `status`          TINYINT      DEFAULT 1 COMMENT '状态：1正常 0禁用',
    `role`            VARCHAR(20)  DEFAULT 'USER' COMMENT '角色：USER普通用户 ADMIN管理员',
    `last_login_time` DATETIME     DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip`   VARCHAR(50)  DEFAULT NULL COMMENT '最后登录IP',
    `deleted`         TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    `created_at`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_wx_openid` (`wx_openid`),
    KEY `idx_inviter_id` (`inviter_id`),
    KEY `idx_invite_code` (`invite_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账号表';

-- ----------------------------
-- 2. 用户资料表
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`       BIGINT       NOT NULL COMMENT '用户ID',
    `nickname`      VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `avatar`        VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `avatar_no`     INT          DEFAULT 1 COMMENT '头像编号（1-8）',
     `gender`        CHAR(1)      NOT NULL COMMENT '性别：M男 F女 · 必填',
    `age`           INT          DEFAULT NULL COMMENT '年龄',
    `birthday`      VARCHAR(20)  DEFAULT NULL COMMENT '生日',
    `city`          VARCHAR(50)  DEFAULT NULL COMMENT '城市',
    `height`        INT          DEFAULT NULL COMMENT '身高(cm)',
    `weight`        INT          DEFAULT NULL COMMENT '体重(kg)',
    `job`           VARCHAR(50)  DEFAULT NULL COMMENT '职业',
    `edu`           VARCHAR(20)  DEFAULT NULL COMMENT '学历',
    `hometown`      VARCHAR(50)  DEFAULT NULL COMMENT '家乡',
    `love_status`   VARCHAR(20)  DEFAULT '单身' COMMENT '情感状态',
    `sign`          VARCHAR(200) DEFAULT NULL COMMENT '个性签名',
    `tags`          JSON         DEFAULT NULL COMMENT '兴趣标签（JSON数组）',
    `fans_count`    INT          DEFAULT 0 COMMENT '粉丝数',
    `follow_count`  INT          DEFAULT 0 COMMENT '关注数',
    `friends_count` INT          DEFAULT 0 COMMENT '好友数',
    `likes_count`   INT          DEFAULT 0 COMMENT '获赞数',
    `created_at`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资料表';

-- ----------------------------
-- 3. 假用户（运营用女性账号）
-- ----------------------------
DROP TABLE IF EXISTS `fake_user`;
CREATE TABLE `fake_user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        VARCHAR(50)  NOT NULL COMMENT '昵称',
    `avatar_no`   INT          DEFAULT 1 COMMENT '头像编号（1-8）',
    `age`         INT          DEFAULT NULL COMMENT '年龄',
    `city`        VARCHAR(50)  DEFAULT NULL COMMENT '城市',
    `dist`        VARCHAR(20)  DEFAULT NULL COMMENT '距离描述',
    `tags`        JSON         DEFAULT NULL COMMENT '兴趣标签（JSON数组）',
    `sign`        VARCHAR(200) DEFAULT NULL COMMENT '个性签名',
    `online`      TINYINT      DEFAULT 1 COMMENT '是否在线',
    `vip`         TINYINT      DEFAULT 0 COMMENT '是否VIP',
    `height`      INT          DEFAULT NULL COMMENT '身高(cm)',
    `weight`      INT          DEFAULT NULL COMMENT '体重(kg)',
    `job`         VARCHAR(50)  DEFAULT NULL COMMENT '职业',
    `edu`         VARCHAR(20)  DEFAULT NULL COMMENT '学历',
    `hometown`    VARCHAR(50)  DEFAULT NULL COMMENT '家乡',
    `love_status` VARCHAR(20)  DEFAULT '单身' COMMENT '情感状态',
    `persona`     VARCHAR(500) DEFAULT NULL COMMENT '人设描述',
    `cs_owner`    VARCHAR(50)  DEFAULT NULL COMMENT '客服认领人',
    `status`      TINYINT      DEFAULT 1 COMMENT '状态：1启用 0停用',
    `created_at`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_online` (`online`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='假用户表';

-- ----------------------------
-- 4. 聊天会话表
-- ----------------------------
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `male_user_id`    BIGINT       NOT NULL COMMENT '男用户ID',
    `female_user_id`  BIGINT       NOT NULL COMMENT '女用户ID',
    `is_fake`         TINYINT      DEFAULT 0 COMMENT '对方是否为假用户',
    `last_msg`        VARCHAR(500) DEFAULT NULL COMMENT '最后一条消息内容',
    `last_time`       VARCHAR(20)  DEFAULT NULL COMMENT '最后消息时间',
    `male_unread`     INT          DEFAULT 0 COMMENT '男用户未读数',
    `female_unread`   INT          DEFAULT 0 COMMENT '女用户未读数',
    `type`            VARCHAR(20)  DEFAULT 'chat' COMMENT '会话类型',
    `deleted`         TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    `created_at`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_male_user` (`male_user_id`),
    KEY `idx_female_user` (`female_user_id`),
    KEY `idx_male_female` (`male_user_id`, `female_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天会话表';

-- ----------------------------
-- 5. 聊天消息表
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `session_id`   BIGINT       NOT NULL COMMENT '会话ID',
    `sender_id`    BIGINT       NOT NULL COMMENT '发送者ID',
    `sender_role`  VARCHAR(20)  NOT NULL COMMENT '发送者角色：male female system',
    `msg_type`     VARCHAR(20)  DEFAULT 'text' COMMENT '消息类型：text gift sys image',
    `content`      TEXT         DEFAULT NULL COMMENT '消息内容',
    `is_read`      TINYINT      DEFAULT 0 COMMENT '是否已读：0未读 1已读',
    `gift_emoji`   VARCHAR(10)  DEFAULT NULL COMMENT '礼物emoji',
    `gift_name`    VARCHAR(50)  DEFAULT NULL COMMENT '礼物名称',
    `cost_diamond` INT          DEFAULT 0 COMMENT '消费钻石数',
    `msg_time`     VARCHAR(20)  DEFAULT NULL COMMENT '消息时间',
    `created_at`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_sender_id` (`sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- ----------------------------
-- 6. 礼物目录表
-- ----------------------------
DROP TABLE IF EXISTS `gift`;
CREATE TABLE `gift` (
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT '礼物ID',
    `name`       VARCHAR(50) NOT NULL COMMENT '礼物名称',
    `icon`       VARCHAR(10) DEFAULT NULL COMMENT '礼物emoji图标',
    `category`   VARCHAR(20) DEFAULT 'romantic' COMMENT '分类：hot romantic luxury',
    `price`      INT         NOT NULL COMMENT '价格（钻石）',
    `sort`       INT         DEFAULT 0 COMMENT '排序',
    `status`     TINYINT     DEFAULT 1 COMMENT '状态：1上架 0下架',
    `created_at` DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼物目录表';

-- ----------------------------
-- 7. 赠送礼物记录表
-- ----------------------------
DROP TABLE IF EXISTS `gift_record`;
CREATE TABLE `gift_record` (
    `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `sender_id`     BIGINT      NOT NULL COMMENT '赠送者ID',
    `receiver_id`   BIGINT      NOT NULL COMMENT '接收者ID',
    `gift_id`       BIGINT      NOT NULL COMMENT '礼物ID',
    `gift_name`     VARCHAR(50) DEFAULT NULL COMMENT '礼物名称',
    `gift_icon`     VARCHAR(10) DEFAULT NULL COMMENT '礼物图标',
    `quantity`      INT         DEFAULT 1 COMMENT '数量',
    `unit_price`    INT         DEFAULT 0 COMMENT '单价（钻石）',
    `total_diamond` INT         DEFAULT 0 COMMENT '总价（钻石）',
    `session_id`    BIGINT      DEFAULT NULL COMMENT '关联会话ID',
    `created_at`    DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='赠送礼物记录表';

-- ----------------------------
-- 8. 钻石流水表
-- ----------------------------
DROP TABLE IF EXISTS `diamond_record`;
CREATE TABLE `diamond_record` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`       BIGINT       NOT NULL COMMENT '用户ID',
    `type`          VARCHAR(10)  NOT NULL COMMENT '类型：in收入 out支出',
    `amount`        INT          NOT NULL COMMENT '钻石数量变化',
    `balance_after` INT          NOT NULL COMMENT '变动后余额',
    `biz_type`      VARCHAR(30)  NOT NULL COMMENT '业务类型',
    `ref_id`        BIGINT       DEFAULT NULL COMMENT '关联业务ID',
    `remark`        VARCHAR(200) DEFAULT NULL COMMENT '备注',
    `created_at`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_biz_type` (`biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钻石流水表';

-- ----------------------------
-- 9. 充值订单表
-- ----------------------------
DROP TABLE IF EXISTS `recharge_order`;
CREATE TABLE `recharge_order` (
    `id`             BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no`       VARCHAR(32)    NOT NULL COMMENT '订单号',
    `user_id`        BIGINT         NOT NULL COMMENT '用户ID',
    `package_id`     BIGINT         DEFAULT NULL COMMENT '套餐ID',
    `amount`         DECIMAL(10,2)  NOT NULL COMMENT '充值金额（元）',
    `diamonds`       INT            NOT NULL COMMENT '获得钻石数',
    `bonus_diamonds` INT            DEFAULT 0 COMMENT '赠送钻石数',
    `pay_method`     VARCHAR(20)    NOT NULL COMMENT '支付方式',
    `status`         VARCHAR(20)    DEFAULT 'pending' COMMENT '支付状态',
    `trade_no`       VARCHAR(100)   DEFAULT NULL COMMENT '第三方支付流水号',
    `paid_at`        DATETIME       DEFAULT NULL COMMENT '支付时间',
    `created_at`     DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值订单表';

-- ----------------------------
-- 10. 提现订单表
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_order`;
CREATE TABLE `withdraw_order` (
    `id`             BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no`       VARCHAR(32)    NOT NULL COMMENT '订单号',
    `user_id`        BIGINT         NOT NULL COMMENT '用户ID',
    `amount`         DECIMAL(10,2)  NOT NULL COMMENT '提现金额（元）',
    `fee`            DECIMAL(10,2)  DEFAULT 0 COMMENT '手续费（元）',
    `actual_amount`  DECIMAL(10,2)  DEFAULT 0 COMMENT '实际到账（元）',
    `method`         VARCHAR(20)    NOT NULL COMMENT '提现方式',
    `account`        VARCHAR(200)   DEFAULT NULL COMMENT '收款账号',
    `status`         VARCHAR(20)    DEFAULT 'pending' COMMENT '状态',
    `audit_remark`   VARCHAR(500)   DEFAULT NULL COMMENT '审核备注',
    `paid_at`        DATETIME       DEFAULT NULL COMMENT '打款时间',
    `created_at`     DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现订单表';

-- ----------------------------
-- 11. VIP订单表
-- ----------------------------
DROP TABLE IF EXISTS `vip_order`;
CREATE TABLE `vip_order` (
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no`    VARCHAR(32)    NOT NULL COMMENT '订单号',
    `user_id`     BIGINT         NOT NULL COMMENT '用户ID',
    `plan_type`   VARCHAR(20)    NOT NULL COMMENT '套餐类型：month quarter year',
    `amount`      DECIMAL(10,2)  NOT NULL COMMENT '支付金额（元）',
    `pay_method`  VARCHAR(20)    NOT NULL COMMENT '支付方式',
    `status`      VARCHAR(20)    DEFAULT 'pending' COMMENT '支付状态',
    `trade_no`    VARCHAR(100)   DEFAULT NULL COMMENT '第三方支付流水号',
    `start_time`  DATETIME       DEFAULT NULL COMMENT 'VIP开始时间',
    `expire_time` DATETIME       DEFAULT NULL COMMENT 'VIP到期时间',
    `paid_at`     DATETIME       DEFAULT NULL COMMENT '支付时间',
    `created_at`  DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP订单表';

-- ----------------------------
-- 12. 邀请记录表
-- ----------------------------
DROP TABLE IF EXISTS `invite_record`;
CREATE TABLE `invite_record` (
    `id`            BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `inviter_id`    BIGINT         NOT NULL COMMENT '邀请人ID',
    `invitee_id`    BIGINT         NOT NULL COMMENT '被邀请人ID',
    `status`        VARCHAR(20)    DEFAULT 'registered' COMMENT '状态',
    `reward_diamond` INT           DEFAULT 0 COMMENT '奖励钻石数',
    `reward_cash`   DECIMAL(10,2)  DEFAULT 0 COMMENT '现金返佣（元）',
    `created_at`    DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_invitee` (`invitee_id`),
    KEY `idx_inviter_id` (`inviter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邀请记录表';

-- ----------------------------
-- 13. 用户钻石余额表
-- ----------------------------
DROP TABLE IF EXISTS `user_balance`;
CREATE TABLE `user_balance` (
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`          BIGINT        NOT NULL COMMENT '用户ID',
    `diamonds`         INT           DEFAULT 0 COMMENT '钻石余额',
    `cash`             DECIMAL(10,2) DEFAULT 0.00 COMMENT '可提现余额（元）',
    `total_recharged`  DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计充值（元）',
    `total_withdrawn`  DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计提现（元）',
    `is_vip`           TINYINT       DEFAULT 0 COMMENT '是否VIP',
    `vip_expire_time`  DATETIME      DEFAULT NULL COMMENT 'VIP到期时间',
    `daily_free_chat`  INT           DEFAULT 0 COMMENT 'VIP当日剩余免费聊天数',
    `is_authed`        TINYINT       DEFAULT 0 COMMENT '是否已实名',
    `created_at`       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户余额表';

-- ----------------------------
-- 14. 推送规则配置表
-- ----------------------------
DROP TABLE IF EXISTS `push_rule`;
CREATE TABLE `push_rule` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `enabled`      TINYINT      DEFAULT 1 COMMENT '是否启用',
    `time_slots`   JSON         DEFAULT NULL COMMENT '时间段配置（JSON数组）',
    `strategy`     VARCHAR(20)  DEFAULT 'active' COMMENT '选人策略',
    `cooldown_min` INT          DEFAULT 30 COMMENT '冷却时间（分钟）',
    `created_at`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送规则表';

-- ----------------------------
-- 15. 推送日志表
-- ----------------------------
DROP TABLE IF EXISTS `push_log`;
CREATE TABLE `push_log` (
    `id`              BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `fake_user_id`    BIGINT      NOT NULL COMMENT '假用户ID',
    `target_user_id`  BIGINT      NOT NULL COMMENT '目标男用户ID',
    `source`          VARCHAR(20) DEFAULT 'auto' COMMENT '来源：manual auto',
    `slot`            VARCHAR(20) DEFAULT NULL COMMENT '对应时段编号',
    `opened`          TINYINT     DEFAULT 0 COMMENT '是否已打开',
    `replied`         TINYINT     DEFAULT 0 COMMENT '是否已回复',
    `created_at`      DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_fake_user_id` (`fake_user_id`),
    KEY `idx_target_user_id` (`target_user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送日志表';

-- ----------------------------
-- 16. 实名认证表
-- ----------------------------
DROP TABLE IF EXISTS `real_name_auth`;
CREATE TABLE `real_name_auth` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`         BIGINT       NOT NULL COMMENT '用户ID',
    `real_name`       VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    `id_card`         VARCHAR(200) DEFAULT NULL COMMENT '身份证号（AES加密存储）',
    `id_card_front`   VARCHAR(500) DEFAULT NULL COMMENT '身份证正面照URL',
    `id_card_back`    VARCHAR(500) DEFAULT NULL COMMENT '身份证反面照URL',
    `face_image`      VARCHAR(500) DEFAULT NULL COMMENT '人脸照片URL',
    `birthday`        VARCHAR(20)  DEFAULT NULL COMMENT '出生日期',
    `status`          VARCHAR(20)  DEFAULT 'pending' COMMENT '审核状态',
    `audit_remark`    VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
    `created_at`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实名认证表';

-- ----------------------------
-- 17. 交易流水表
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
    `id`             BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`        BIGINT         NOT NULL COMMENT '用户ID',
    `type`           VARCHAR(10)    NOT NULL COMMENT '类型：income收入 expense支出',
    `amount`         DECIMAL(10,2)  NOT NULL COMMENT '金额（元）',
    `biz_type`       VARCHAR(30)    NOT NULL COMMENT '业务类型',
    `ref_id`         BIGINT         DEFAULT NULL COMMENT '关联业务ID',
    `description`    VARCHAR(200)   DEFAULT NULL COMMENT '交易描述',
    `balance_after`  DECIMAL(10,2)  DEFAULT NULL COMMENT '交易后余额',
    `created_at`     DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';

-- ----------------------------
-- 初始数据：礼物目录
-- ----------------------------
INSERT INTO `gift` (`name`, `icon`, `category`, `price`, `sort`) VALUES
('玫瑰花',  '🌹', 'romantic', 66,   1),
('巧克力',  '🍫', 'romantic', 88,   2),
('水晶鞋',  '👠', 'romantic', 128,  3),
('拥抱',    '🤗', 'romantic', 166,  4),
('戒指',    '💍', 'luxury',   520,  5),
('跑车',    '🏎️', 'luxury',  1314, 6),
('城堡',    '🏰', 'luxury',  5200, 7),
('皇冠',    '👑', 'luxury',  9999, 8),
('香水',    '💐', 'romantic', 199,  9),
('口红',    '💄', 'romantic', 266,  10),
('项链',    '📿', 'luxury',   888,  11),
('包包',    '👜', 'luxury',   1888,12);

-- ----------------------------
-- 初始数据：默认推送规则
-- ----------------------------
INSERT INTO `push_rule` (`enabled`, `time_slots`, `strategy`, `cooldown_min`) VALUES
(1, '[{"start":"09:00","end":"11:00","maxPer":1},{"start":"12:00","end":"14:00","maxPer":1},{"start":"19:00","end":"22:30","maxPer":2}]', 'active', 30);

-- ----------------------------
-- 初始数据：管理员账号（密码 admin123，BCrypt加密）
-- ----------------------------
INSERT INTO `user` (`phone`, `password`, `role`, `status`) VALUES
('13800000000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E5', 'ADMIN', 1);
