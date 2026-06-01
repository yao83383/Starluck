package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.service.VipService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vip")
public class VipController {

    private final VipService vipService;

    public VipController(VipService vipService) {
        this.vipService = vipService;
    }

    @GetMapping("/plans")
    public Result<Map<String, Object>> getPlans() {
        return Result.ok(vipService.getPlans());
    }

    @PostMapping("/subscribe")
    public Result<Void> subscribe(@RequestParam String planType, @RequestParam String payMethod) {
        Long userId = SecurityUtil.getCurrentUserId();
        vipService.subscribe(userId, planType, payMethod);
        return Result.okMsg("VIP开通成功");
    }
}
