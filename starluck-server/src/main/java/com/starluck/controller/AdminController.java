package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.entity.FakeUser;
import com.starluck.entity.PushRule;
import com.starluck.service.AdminService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    /**
     * 为假用户上传头像
     */
    @PostMapping("/fake-users/{id}/avatar")
    public Result<Map<String, String>> uploadFakeAvatar(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) {
        try {
            String ext = file.getOriginalFilename() != null
                    && file.getOriginalFilename().contains(".")
                    ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
                    : ".png";
            String fileName = "fake_avatar_" + id + "_" + UUID.randomUUID().toString().substring(0, 8) + ext;
            File dir = new File("/data/uploads/avatars");
            if (!dir.exists()) dir.mkdirs();
            File dest = new File(dir, fileName);
            file.transferTo(dest);

            // 更新假用户头像（cosmetic avatar_url 可以忽略，但后端留痕）
            FakeUser fu = new FakeUser();
            fu.setId(id);
            fu.setAvatarNo(id.intValue() % 8 + 1);
            adminService.saveFakeUser(fu);
            return Result.ok(Map.of("url", "/uploads/avatars/" + fileName));
        } catch (IOException e) {
            return Result.fail("头像上传失败：" + e.getMessage());
        }
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
