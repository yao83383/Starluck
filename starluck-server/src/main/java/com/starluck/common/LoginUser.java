package com.starluck.common;

import lombok.Data;

/**
 * 当前登录用户上下文
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
public class LoginUser {

    private Long userId;
    private String phone;

    public static LoginUser of(Long userId, String phone) {
        LoginUser user = new LoginUser();
        user.userId = userId;
        user.phone = phone;
        return user;
    }
}
