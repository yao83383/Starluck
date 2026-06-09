package com.starluck.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.config.ChatWebSocketHandler;
import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.entity.ChatMessage;
import com.starluck.entity.ChatSession;
import com.starluck.entity.FakeUser;
import com.starluck.entity.PushRule;
import com.starluck.entity.User;
import com.starluck.mapper.ChatMessageMapper;
import com.starluck.mapper.ChatSessionMapper;
import com.starluck.service.AdminService;
import com.starluck.vo.ChatMessageVO;
import com.starluck.vo.MaleUserVO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;

    public AdminController(AdminService adminService, ChatSessionMapper sessionMapper,
                           ChatMessageMapper messageMapper) {
        this.adminService = adminService;
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
    }

    @GetMapping("/fake-users")
    public Result<List<FakeUser>> getFakeUsers(@RequestParam(required = false) String keyword) {
        Long userId = SecurityUtil.getCurrentUserId();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return Result.ok(adminService.getFakeUsers(keyword, isAdmin ? null : userId));
    }

    @GetMapping("/fake-users/cs-list")
    public Result<List<User>> getCsList() {
        return Result.ok(adminService.getCsList());
    }

    @PutMapping("/fake-users/{id}/assign")
    public Result<Void> assignFake(@PathVariable Long id, @RequestParam(required = false) Long csUserId,
                                    @RequestParam(required = false) String csName) {
        adminService.assignFakeToCs(id, csUserId, csName);
        return Result.okMsg("分配成功");
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

    @GetMapping("/sessions")
    public Result<List<MaleUserVO>> getAdminSessions() {
        Long userId = SecurityUtil.getCurrentUserId();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return Result.ok(adminService.getMaleUsers(isAdmin ? null : userId));
    }

    @PostMapping("/chat/send")
    public Result<Void> sendAsCs(@RequestBody Map<String, Object> body) {
        Long sessionId = Long.valueOf(body.get("sessionId").toString());
        String content = (String) body.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.fail("消息不能为空");
        }
        adminService.sendAsCs(sessionId, content.trim());
        return Result.okMsg("发送成功");
    }

    @GetMapping("/chat/messages/{sessionId}")
    public Result<List<ChatMessageVO>> getAdminChatMessages(@PathVariable Long sessionId) {
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) throw new BusinessException("会话不存在");

        List<ChatMessage> messages = messageMapper.selectList(
                new LambdaQueryWrapper<ChatMessage>()
                        .eq(ChatMessage::getSessionId, sessionId)
                        .orderByAsc(ChatMessage::getCreatedAt));

        return Result.ok(messages.stream().map(m -> ChatMessageVO.builder()
                .id(m.getId()).sessionId(m.getSessionId()).senderId(m.getSenderId())
                .senderRole(m.getSenderRole()).msgType(m.getMsgType()).content(m.getContent())
                .isRead(m.getIsRead() != null ? m.getIsRead() : 0)
                .giftEmoji(m.getGiftEmoji()).giftName(m.getGiftName())
                .costDiamond(m.getCostDiamond()).msgTime(m.getMsgTime())
                .build()).collect(Collectors.toList()));
    }

    @PostMapping("/chat/mark-read/{sessionId}")
    @Transactional
    public Result<Void> markRead(@PathVariable Long sessionId) {
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) throw new BusinessException("会话不存在");
        session.setFemaleUnread(0);
        sessionMapper.updateById(session);
        messageMapper.markAsRead(sessionId, session.getMaleUserId());
        Map<String, Object> push = new HashMap<>();
        push.put("type", "messages_read");
        push.put("sessionId", sessionId);
        ChatWebSocketHandler.sendToUser(session.getMaleUserId(), JSONUtil.toJsonStr(push));
        return Result.ok();
    }
}
