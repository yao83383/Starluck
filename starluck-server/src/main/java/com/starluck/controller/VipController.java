package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.service.VipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * VIP控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/vip")
@RequiredArgsConstructor
public class VipController {

    private final VipService vipService;

    /**
     * 获取VIP套餐列表
     */
    @GetMapping("/plans")
    public Result<Map<String, Object>> getPlans() {
        return Result.ok(vipService.getPlans());
    }

    /**
     * 开通VIP
     */
    @PostMapping("/subscribe")
    public Result<Void> subscribe(@RequestParam String planType, @RequestParam String payMethod) {
        Long userId = SecurityUtil.getCurrentUserId();
        vipService.subscribe(userId, planType, payMethod);
        return Result.ok("VIP开通成功");
    }
}
