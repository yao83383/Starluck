package com.starluck.service;

import com.starluck.dto.GiftSendRequest;
import com.starluck.entity.Gift;

import java.util.List;

/**
 * 礼物服务接口
 *
 * @author AI
 * @date 2026-06-01
 */
public interface GiftService {

    /**
     * 获取礼物列表
     */
    List<Gift> getGiftList(String category);

    /**
     * 赠送礼物
     */
    void sendGift(Long senderId, GiftSendRequest request);
}
