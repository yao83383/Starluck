package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.entity.FakeUser;
import com.starluck.entity.PushRule;
import com.starluck.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /** 假用户列表 */
    @GetMapping("/fake-users")
    public Result<List<FakeUser>> getFakeUsers(@RequestParam(required = false) String keyword) {
        return Result.ok(adminService.getFakeUsers(keyword));
    }

    /** 新增/更新假用户 */
    @PostMapping("/fake-users")
    public Result<FakeUser> saveFakeUser(@RequestBody FakeUser fakeUser) {
        return Result.ok(adminService.saveFakeUser(fakeUser));
    }

    /** 删除假用户 */
    @DeleteMapping("/fake-users/{id}")
    public Result<Void> deleteFakeUser(@PathVariable Long id) {
        adminService.deleteFakeUser(id);
        return Result.ok();
    }

    /** 批量生成假用户 */
    @PostMapping("/fake-users/batch")
    public Result<List<FakeUser>> batchGenerate(@RequestParam(defaultValue = "5") int count) {
        return Result.ok(adminService.batchGenerate(count));
    }

    /** 手动推送 */
    @PostMapping("/push/manual")
    public Result<Void> manualPush(@RequestParam Long fakeUserId, @RequestParam Long targetUserId) {
        adminService.manualPush(fakeUserId, targetUserId);
        return Result.okMsg("推送成功");
    }

    /** 获取推送规则 */
    @GetMapping("/push/rule")
    public Result<PushRule> getPushRule() {
        return Result.ok(adminService.getPushRule());
    }

    /** 更新推送规则 */
    @PutMapping("/push/rule")
    public Result<Void> updatePushRule(@RequestBody PushRule rule) {
        adminService.updatePushRule(rule);
        return Result.ok();
    }

    /** AI建议回复 */
    @PostMapping("/ai/suggest")
    public Result<String> getAiSuggestion(@RequestParam Long sessionId) {
        return Result.ok(adminService.getAiSuggestion(sessionId));
    }
}
