package com.starluck.controller;

import com.starluck.common.BusinessException;
import com.starluck.common.Result;
import com.starluck.dto.AdminLoginRequest;
import com.starluck.entity.User;
import com.starluck.mapper.UserMapper;
import com.starluck.security.JwtUtil;
import com.starluck.vo.LoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminAuthController(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody AdminLoginRequest request) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getPhone, request.getPhone())
                        .eq(User::getStatus, 1));
        if (user == null) {
            throw new BusinessException("账号不存在或已禁用");
        }

        String role = user.getRole();
        if (!"ADMIN".equals(role) && !"CS".equals(role)) {
            throw new BusinessException("非管理员账号");
        }

        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), role);

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setOnboarded(true);
        vo.setRole(role);
        return Result.ok("登录成功", vo);
    }
}
