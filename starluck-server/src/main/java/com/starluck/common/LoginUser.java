package com.starluck.common;

/**
 * 当前登录用户上下文
 *
 * @author AI
 * @date 2026-06-01
 */
public class LoginUser {

    private Long userId;
    private String phone;

    public static LoginUser of(Long userId, String phone) {
        LoginUser user = new LoginUser();
        user.userId = userId;
        user.phone = phone;
        return user;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
