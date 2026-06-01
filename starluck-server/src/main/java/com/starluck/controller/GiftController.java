package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.GiftSendRequest;
import com.starluck.entity.Gift;
import com.starluck.service.GiftService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gift")
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @GetMapping("/list")
    public Result<List<Gift>> getList(@RequestParam(required = false) String category) {
        return Result.ok(giftService.getGiftList(category));
    }

    @PostMapping("/send")
    public Result<Void> sendGift(@Valid @RequestBody GiftSendRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        giftService.sendGift(userId, request);
        return Result.okMsg("礼物已送出");
    }
}
