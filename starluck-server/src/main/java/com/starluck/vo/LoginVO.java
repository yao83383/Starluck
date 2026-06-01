package com.starluck.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@Builder
public class LoginVO {

    private String token;

    private Long userId;

    private String phone;

    private String nickname;

    private String avatar;

    private Boolean onboarded;

    private Integer diamonds;

    private Boolean isVip;

    private String vipExpireTime;
}
