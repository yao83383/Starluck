package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.entity.UserProfile;
import com.starluck.service.UserService;
import com.starluck.vo.UserProfileVO;
import org.springframework.web.bind.annotation.*;

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
}
