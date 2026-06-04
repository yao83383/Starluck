package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.LoginRequest;
import com.starluck.dto.SendCodeRequest;
import com.starluck.service.AuthService;
import com.starluck.vo.LoginVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-code")
    public Result<String> sendCode(@Valid @RequestBody SendCodeRequest request) {
        String code = authService.sendCode(request.getPhone());
        return Result.ok("验证码已发送", code);
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        LoginVO vo = authService.loginByPhone(request);
        return Result.ok("登录成功", vo);
    }

    @PostMapping("/login-by-password")
    public Result<LoginVO> loginByPassword(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String password = body.get("password");
        if (phone == null || phone.isEmpty()) {
            return Result.fail("手机号不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.fail("密码不能为空");
        }
        LoginVO vo = authService.loginByPassword(phone, password);
        return Result.ok("登录成功", vo);
    }

    @GetMapping("/user-info")
    public Result<LoginVO> getUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        LoginVO vo = authService.getUserInfo(userId);
        return Result.ok(vo);
    }
}
