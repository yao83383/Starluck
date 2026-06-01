-- =============================================
-- H2 开发环境建表脚本（MySQL兼容模式）
-- =============================================

CREATE TABLE IF NOT EXISTS `user` (
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `phone`           VARCHAR(20)  DEFAULT NULL,
    `password`        VARCHAR(200) DEFAULT NULL,
    `wx_openid`       VARCHAR(100) DEFAULT NULL,
    `wx_unionid`      VARCHAR(100) DEFAULT NULL,
    `invite_code`     VARCHAR(20)  DEFAULT NULL,
    `inviter_id`      BIGINT       DEFAULT NULL,
    `status`          TINYINT      DEFAULT 1,
    `role`            VARCHAR(20)  DEFAULT 'USER',
    `last_login_time` TIMESTAMP    DEFAULT NULL,
    `last_login_ip`   VARCHAR(50)  DEFAULT NULL,
    `deleted`         TINYINT      DEFAULT 0,
    `created_at`      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `user_profile` (
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`       BIGINT       NOT NULL,
    `nickname`      VARCHAR(50)  DEFAULT NULL,
    `avatar`        VARCHAR(500) DEFAULT NULL,
    `avatar_no`     INT          DEFAULT 1,
    `gender`        CHAR(1)      DEFAULT NULL,
    `age`           INT          DEFAULT NULL,
    `birthday`      VARCHAR(20)  DEFAULT NULL,
    `city`          VARCHAR(50)  DEFAULT NULL,
    `height`        INT          DEFAULT NULL,
    `weight`        INT          DEFAULT NULL,
    `job`           VARCHAR(50)  DEFAULT NULL,
    `edu`           VARCHAR(20)  DEFAULT NULL,
    `hometown`      VARCHAR(50)  DEFAULT NULL,
    `love_status`   VARCHAR(20)  DEFAULT '单身',
    `sign`          VARCHAR(200) DEFAULT NULL,
    `tags`          CLOB         DEFAULT NULL,
    `fans_count`    INT          DEFAULT 0,
    `follow_count`  INT          DEFAULT 0,
    `friends_count` INT          DEFAULT 0,
    `likes_count`   INT          DEFAULT 0,
    `created_at`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `fake_user` (
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(50)  NOT NULL,
    `avatar_no`   INT          DEFAULT 1,
    `age`         INT          DEFAULT NULL,
    `city`        VARCHAR(50)  DEFAULT NULL,
    `dist`        VARCHAR(20)  DEFAULT NULL,
    `tags`        CLOB         DEFAULT NULL,
    `sign`        VARCHAR(200) DEFAULT NULL,
    `online`      TINYINT      DEFAULT 1,
    `vip`         TINYINT      DEFAULT 0,
    `height`      INT          DEFAULT NULL,
    `weight`      INT          DEFAULT NULL,
    `job`         VARCHAR(50)  DEFAULT NULL,
    `edu`         VARCHAR(20)  DEFAULT NULL,
    `hometown`    VARCHAR(50)  DEFAULT NULL,
    `love_status` VARCHAR(20)  DEFAULT '单身',
    `persona`     VARCHAR(500) DEFAULT NULL,
    `cs_owner`    VARCHAR(50)  DEFAULT NULL,
    `status`      TINYINT      DEFAULT 1,
    `created_at`  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `chat_session` (
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `male_user_id`    BIGINT       NOT NULL,
    `female_user_id`  BIGINT       NOT NULL,
    `is_fake`         TINYINT      DEFAULT 0,
    `last_msg`        VARCHAR(500) DEFAULT NULL,
    `last_time`       VARCHAR(20)  DEFAULT NULL,
    `male_unread`     INT          DEFAULT 0,
    `female_unread`   INT          DEFAULT 0,
    `type`            VARCHAR(20)  DEFAULT 'chat',
    `deleted`         TINYINT      DEFAULT 0,
    `created_at`      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `chat_message` (
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `session_id`   BIGINT       NOT NULL,
    `sender_id`    BIGINT       NOT NULL,
    `sender_role`  VARCHAR(20)  NOT NULL,
    `msg_type`     VARCHAR(20)  DEFAULT 'text',
    `content`      CLOB         DEFAULT NULL,
    `gift_emoji`   VARCHAR(10)  DEFAULT NULL,
    `gift_name`    VARCHAR(50)  DEFAULT NULL,
    `cost_diamond` INT          DEFAULT 0,
    `msg_time`     VARCHAR(20)  DEFAULT NULL,
    `created_at`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `gift` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(50) NOT NULL,
    `icon`       VARCHAR(10) DEFAULT NULL,
    `category`   VARCHAR(20) DEFAULT 'romantic',
    `price`      INT         NOT NULL,
    `sort`       INT         DEFAULT 0,
    `status`     TINYINT     DEFAULT 1,
    `created_at` TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `gift_record` (
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `sender_id`     BIGINT      NOT NULL,
    `receiver_id`   BIGINT      NOT NULL,
    `gift_id`       BIGINT      NOT NULL,
    `gift_name`     VARCHAR(50) DEFAULT NULL,
    `gift_icon`     VARCHAR(10) DEFAULT NULL,
    `quantity`      INT         DEFAULT 1,
    `unit_price`    INT         DEFAULT 0,
    `total_diamond` INT         DEFAULT 0,
    `session_id`    BIGINT      DEFAULT NULL,
    `created_at`    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `diamond_record` (
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`       BIGINT       NOT NULL,
    `type`          VARCHAR(10)  NOT NULL,
    `amount`        INT          NOT NULL,
    `balance_after` INT          NOT NULL,
    `biz_type`      VARCHAR(30)  NOT NULL,
    `ref_id`        BIGINT       DEFAULT NULL,
    `remark`        VARCHAR(200) DEFAULT NULL,
    `created_at`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `recharge_order` (
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_no`       VARCHAR(32)    NOT NULL,
    `user_id`        BIGINT         NOT NULL,
    `package_id`     BIGINT         DEFAULT NULL,
    `amount`         DECIMAL(10,2)  NOT NULL,
    `diamonds`       INT            NOT NULL,
    `bonus_diamonds` INT            DEFAULT 0,
    `pay_method`     VARCHAR(20)    NOT NULL,
    `status`         VARCHAR(20)    DEFAULT 'pending',
    `trade_no`       VARCHAR(100)   DEFAULT NULL,
    `paid_at`        TIMESTAMP      DEFAULT NULL,
    `created_at`     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `withdraw_order` (
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_no`       VARCHAR(32)    NOT NULL,
    `user_id`        BIGINT         NOT NULL,
    `amount`         DECIMAL(10,2)  NOT NULL,
    `fee`            DECIMAL(10,2)  DEFAULT 0,
    `actual_amount`  DECIMAL(10,2)  DEFAULT 0,
    `method`         VARCHAR(20)    NOT NULL,
    `account`        VARCHAR(200)   DEFAULT NULL,
    `status`         VARCHAR(20)    DEFAULT 'pending',
    `audit_remark`   VARCHAR(500)   DEFAULT NULL,
    `paid_at`        TIMESTAMP      DEFAULT NULL,
    `created_at`     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `vip_order` (
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_no`    VARCHAR(32)    NOT NULL,
    `user_id`     BIGINT         NOT NULL,
    `plan_type`   VARCHAR(20)    NOT NULL,
    `amount`      DECIMAL(10,2)  NOT NULL,
    `pay_method`  VARCHAR(20)    NOT NULL,
    `status`      VARCHAR(20)    DEFAULT 'pending',
    `trade_no`    VARCHAR(100)   DEFAULT NULL,
    `start_time`  TIMESTAMP      DEFAULT NULL,
    `expire_time` TIMESTAMP      DEFAULT NULL,
    `paid_at`     TIMESTAMP      DEFAULT NULL,
    `created_at`  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `invite_record` (
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `inviter_id`    BIGINT         NOT NULL,
    `invitee_id`    BIGINT         NOT NULL,
    `status`        VARCHAR(20)    DEFAULT 'registered',
    `reward_diamond` INT           DEFAULT 0,
    `reward_cash`   DECIMAL(10,2)  DEFAULT 0,
    `created_at`    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `user_balance` (
    `id`               BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`          BIGINT        NOT NULL,
    `diamonds`         INT           DEFAULT 0,
    `cash`             DECIMAL(10,2) DEFAULT 0.00,
    `total_recharged`  DECIMAL(10,2) DEFAULT 0.00,
    `total_withdrawn`  DECIMAL(10,2) DEFAULT 0.00,
    `is_vip`           TINYINT       DEFAULT 0,
    `vip_expire_time`  TIMESTAMP     DEFAULT NULL,
    `daily_free_chat`  INT           DEFAULT 0,
    `is_authed`        TINYINT       DEFAULT 0,
    `created_at`       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `push_rule` (
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `enabled`      TINYINT      DEFAULT 1,
    `time_slots`   CLOB         DEFAULT NULL,
    `strategy`     VARCHAR(20)  DEFAULT 'active',
    `cooldown_min` INT          DEFAULT 30,
    `created_at`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `push_log` (
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `fake_user_id`    BIGINT      NOT NULL,
    `target_user_id`  BIGINT      NOT NULL,
    `source`          VARCHAR(20) DEFAULT 'auto',
    `slot`            VARCHAR(20) DEFAULT NULL,
    `opened`          TINYINT     DEFAULT 0,
    `replied`         TINYINT     DEFAULT 0,
    `created_at`      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `real_name_auth` (
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`         BIGINT       NOT NULL,
    `real_name`       VARCHAR(50)  DEFAULT NULL,
    `id_card`         VARCHAR(200) DEFAULT NULL,
    `id_card_front`   VARCHAR(500) DEFAULT NULL,
    `id_card_back`    VARCHAR(500) DEFAULT NULL,
    `face_image`      VARCHAR(500) DEFAULT NULL,
    `birthday`        VARCHAR(20)  DEFAULT NULL,
    `status`          VARCHAR(20)  DEFAULT 'pending',
    `audit_remark`    VARCHAR(500) DEFAULT NULL,
    `created_at`      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `transaction` (
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`        BIGINT         NOT NULL,
    `type`           VARCHAR(10)    NOT NULL,
    `amount`         DECIMAL(10,2)  NOT NULL,
    `biz_type`       VARCHAR(30)    NOT NULL,
    `ref_id`         BIGINT         DEFAULT NULL,
    `description`    VARCHAR(200)   DEFAULT NULL,
    `balance_after`  DECIMAL(10,2)  DEFAULT NULL,
    `created_at`     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);
