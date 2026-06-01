package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.entity.UserProfile;
import com.starluck.service.UserService;
import com.starluck.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取个人资料
     */
    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(userService.getUserProfile(userId));
    }

    /**
     * 更新个人资料
     */
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody UserProfile profile) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateProfile(userId, profile);
        return Result.ok("资料更新成功");
    }

    /**
     * 获取他人资料（发现页查看用户主页）
     */
    @GetMapping("/profile/{userId}")
    public Result<UserProfileVO> getOtherProfile(@PathVariable Long userId) {
        return Result.ok(userService.getUserProfile(userId));
    }
}
