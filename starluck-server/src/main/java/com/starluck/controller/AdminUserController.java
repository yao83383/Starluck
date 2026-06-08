package com.starluck.controller;

import com.starluck.common.BusinessException;
import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.entity.User;
import com.starluck.mapper.FakeUserMapper;
import com.starluck.mapper.UserMapper;
import com.starluck.vo.AdminUserVO;
import com.starluck.vo.CsStatsVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final UserMapper userMapper;
    private final FakeUserMapper fakeUserMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(UserMapper userMapper, FakeUserMapper fakeUserMapper,
                               PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.fakeUserMapper = fakeUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public Result<List<AdminUserVO>> listUsers() {
        List<User> users = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .in(User::getRole, "ADMIN", "CS")
                        .orderByAsc(User::getId));

        List<AdminUserVO> list = users.stream().map(u -> {
            Long fakeCount = fakeUserMapper.selectCount(
                    new LambdaQueryWrapper<com.starluck.entity.FakeUser>()
                            .eq(com.starluck.entity.FakeUser::getCsOwner, String.valueOf(u.getId())));
            return AdminUserVO.builder()
                    .id(u.getId())
                    .phone(u.getPhone())
                    .role(u.getRole())
                    .status(u.getStatus())
                    .fakeUserCount(fakeCount.intValue())
                    .lastLoginTime(u.getLastLoginTime() != null
                            ? u.getLastLoginTime().format(DATE_FMT) : null)
                    .build();
        }).collect(Collectors.toList());
        return Result.ok(list);
    }

    @PostMapping("/users")
    public Result<AdminUserVO> createUser(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String password = body.get("password");
        String role = body.get("role");
        if (phone == null || phone.isEmpty()) throw new BusinessException("手机号不能为空");
        if (password == null || password.isEmpty()) throw new BusinessException("密码不能为空");
        if (!"ADMIN".equals(role) && !"CS".equals(role)) throw new BusinessException("角色无效");

        User exist = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (exist != null) throw new BusinessException("手机号已存在");

        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus(1);
        userMapper.insert(user);

        return Result.ok(AdminUserVO.builder()
                .id(user.getId()).phone(phone).role(role).status(1).build());
    }

    @PutMapping("/users/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userMapper.selectById(id);
        if (user == null) throw new BusinessException("用户不存在");

        String role = body.get("role");
        String statusStr = body.get("status");
        if (role != null) user.setRole(role);
        if (statusStr != null) user.setStatus(Integer.parseInt(statusStr));
        userMapper.updateById(user);
        return Result.ok();
    }

    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId.equals(id)) throw new BusinessException("不能删除自己");
        userMapper.deleteById(id);
        return Result.ok();
    }

    @PutMapping("/users/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userMapper.selectById(id);
        if (user == null) throw new BusinessException("用户不存在");
        String newPassword = body.get("password");
        if (newPassword == null || newPassword.isEmpty()) throw new BusinessException("新密码不能为空");
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
        return Result.ok();
    }

    @GetMapping("/users/me/stats")
    public Result<CsStatsVO> csSelfStats() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(buildCsStats(userId));
    }

    @GetMapping("/users/{id}/stats")
    public Result<CsStatsVO> csStats(@PathVariable Long id) {
        return Result.ok(buildCsStats(id));
    }

    private CsStatsVO buildCsStats(Long userId) {
        String csOwnerStr = String.valueOf(userId);
        Long fakeCount = fakeUserMapper.selectCount(
                new LambdaQueryWrapper<com.starluck.entity.FakeUser>()
                        .eq(com.starluck.entity.FakeUser::getCsOwner, csOwnerStr));

        User u = userMapper.selectById(userId);
        return CsStatsVO.builder()
                .userId(userId)
                .phone(u != null ? u.getPhone() : "")
                .fakeUserCount(fakeCount.intValue())
                .totalPushCount(0)
                .openedCount(0)
                .repliedCount(0)
                .totalCashEarned(BigDecimal.ZERO)
                .build();
    }
}
