package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.entity.FakeUser;
import com.starluck.entity.PushRule;
import com.starluck.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/fake-users")
    public Result<List<FakeUser>> getFakeUsers(@RequestParam(required = false) String keyword) {
        return Result.ok(adminService.getFakeUsers(keyword));
    }

    @PostMapping("/fake-users")
    public Result<FakeUser> saveFakeUser(@RequestBody FakeUser fakeUser) {
        return Result.ok(adminService.saveFakeUser(fakeUser));
    }

    @DeleteMapping("/fake-users/{id}")
    public Result<Void> deleteFakeUser(@PathVariable Long id) {
        adminService.deleteFakeUser(id);
        return Result.ok();
    }

    @PostMapping("/fake-users/batch")
    public Result<List<FakeUser>> batchGenerate(@RequestParam(defaultValue = "5") int count) {
        return Result.ok(adminService.batchGenerate(count));
    }

    @PostMapping("/push/manual")
    public Result<Void> manualPush(@RequestParam Long fakeUserId, @RequestParam Long targetUserId) {
        adminService.manualPush(fakeUserId, targetUserId);
        return Result.okMsg("推送成功");
    }

    @GetMapping("/push/rule")
    public Result<PushRule> getPushRule() {
        return Result.ok(adminService.getPushRule());
    }

    @PutMapping("/push/rule")
    public Result<Void> updatePushRule(@RequestBody PushRule rule) {
        adminService.updatePushRule(rule);
        return Result.ok();
    }

    @PostMapping("/ai/suggest")
    public Result<String> getAiSuggestion(@RequestParam Long sessionId) {
        return Result.ok(adminService.getAiSuggestion(sessionId));
    }
}
