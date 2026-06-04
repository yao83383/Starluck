package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.entity.UserProfile;
import com.starluck.service.UserService;
import com.starluck.vo.UserCardVO;
import com.starluck.vo.UserProfileVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody UserProfile profile) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateProfile(userId, profile);
        return Result.okMsg("资料更新成功");
    }

    @GetMapping("/profile/{userId}")
    public Result<UserProfileVO> getOtherProfile(@PathVariable Long userId) {
        return Result.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/discover")
    public Result<List<UserCardVO>> discover() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(userService.getDiscoverList(userId));
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> body) {
        Long userId = SecurityUtil.getCurrentUserId();
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.okMsg("密码设置成功");
    }
}
