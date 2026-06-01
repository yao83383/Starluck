package com.starluck.service;

import com.starluck.dto.LoginRequest;
import com.starluck.vo.LoginVO;

/**
 * 认证服务接口
 *
 * @author AI
 * @date 2026-06-01
 */
public interface AuthService {

    /**
     * 发送短信验证码
     */
    void sendCode(String phone);

    /**
     * 手机号验证码登录（无账号则自动注册）
     */
    LoginVO loginByPhone(LoginRequest request);

    /**
     * 获取登录用户信息
     */
    LoginVO getUserInfo(Long userId);
}
