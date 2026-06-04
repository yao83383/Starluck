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
    /**
     * 发送短信验证码
     * @return 验证码（开发模式直接返回，生产模式返回空字符串）
     */
    String sendCode(String phone);

    /**
     * 手机号验证码登录（无账号则自动注册）
     */
    /**
     * 手机号验证码登录
     */
    LoginVO loginByPhone(LoginRequest request);

    /**
     * 手机号密码登录
     */
    LoginVO loginByPassword(String phone, String password);

    /**
     * 获取用户信息
     */
    LoginVO getUserInfo(Long userId);
}
