package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.LoginRequest;
import com.starluck.dto.SendCodeRequest;
import com.starluck.service.AuthService;
import com.starluck.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 发送短信验证码
     */
    @PostMapping("/send-code")
    public Result<Void> sendCode(@Valid @RequestBody SendCodeRequest request) {
        authService.sendCode(request.getPhone());
        return Result.okMsg("验证码已发送");
    }

    /**
     * 手机号验证码登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        LoginVO vo = authService.loginByPhone(request);
        return Result.ok("登录成功", vo);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/user-info")
    public Result<LoginVO> getUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        LoginVO vo = authService.getUserInfo(userId);
        return Result.ok(vo);
    }
}
