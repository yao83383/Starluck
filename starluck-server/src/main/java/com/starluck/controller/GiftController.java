package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.GiftSendRequest;
import com.starluck.entity.Gift;
import com.starluck.service.GiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 礼物控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/gift")
@RequiredArgsConstructor
public class GiftController {

    private final GiftService giftService;

    /**
     * 获取礼物列表
     */
    @GetMapping("/list")
    public Result<List<Gift>> getList(@RequestParam(required = false) String category) {
        return Result.ok(giftService.getGiftList(category));
    }

    /**
     * 赠送礼物
     */
    @PostMapping("/send")
    public Result<Void> sendGift(@Valid @RequestBody GiftSendRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        giftService.sendGift(userId, request);
        return Result.okMsg("礼物已送出");
    }
}
