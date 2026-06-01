package com.starluck.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.config.ChatWebSocketHandler;
import com.starluck.dto.GiftSendRequest;
import com.starluck.entity.DiamondRecord;
import com.starluck.entity.Gift;
import com.starluck.entity.GiftRecord;
import com.starluck.entity.Transaction;
import com.starluck.entity.UserBalance;
import com.starluck.mapper.DiamondRecordMapper;
import com.starluck.mapper.GiftMapper;
import com.starluck.mapper.GiftRecordMapper;
import com.starluck.mapper.TransactionMapper;
import com.starluck.mapper.UserBalanceMapper;
import com.starluck.service.GiftService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 礼物服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Service
public class GiftServiceImpl implements GiftService {

    private final GiftMapper giftMapper;
    private final GiftRecordMapper giftRecordMapper;
    private final UserBalanceMapper balanceMapper;
    private final DiamondRecordMapper diamondRecordMapper;
    private final TransactionMapper transactionMapper;

    public GiftServiceImpl(GiftMapper giftMapper, GiftRecordMapper giftRecordMapper,
                           UserBalanceMapper balanceMapper, DiamondRecordMapper diamondRecordMapper,
                           TransactionMapper transactionMapper) {
        this.giftMapper = giftMapper;
        this.giftRecordMapper = giftRecordMapper;
        this.balanceMapper = balanceMapper;
        this.diamondRecordMapper = diamondRecordMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public List<Gift> getGiftList(String category) {
        LambdaQueryWrapper<Gift> wrapper = new LambdaQueryWrapper<Gift>()
                .eq(Gift::getStatus, 1)
                .orderByAsc(Gift::getSort);
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Gift::getCategory, category);
        }
        return giftMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendGift(Long senderId, GiftSendRequest request) {
        Gift gift = giftMapper.selectById(request.getGiftId());
        if (gift == null || gift.getStatus() == 0) {
            throw new BusinessException("礼物不存在或已下架");
        }

        int quantity = request.getQuantity() != null ? request.getQuantity() : 1;
        int totalCost = gift.getPrice() * quantity;

        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, senderId));
        if (balance == null || balance.getDiamonds() < totalCost) {
            throw new BusinessException("钻石余额不足");
        }
        balance.setDiamonds(balance.getDiamonds() - totalCost);
        balanceMapper.updateById(balance);

        DiamondRecord dr = new DiamondRecord();
        dr.setUserId(senderId);
        dr.setType("out");
        dr.setAmount(-totalCost);
        dr.setBalanceAfter(balance.getDiamonds());
        dr.setBizType("gift");
        dr.setRefId(gift.getId());
        dr.setRemark("赠送礼物：" + gift.getName() + " x" + quantity);
        diamondRecordMapper.insert(dr);

        GiftRecord record = new GiftRecord();
        record.setSenderId(senderId);
        record.setReceiverId(request.getReceiverId());
        record.setGiftId(gift.getId());
        record.setGiftName(gift.getName());
        record.setGiftIcon(gift.getIcon());
        record.setQuantity(quantity);
        record.setUnitPrice(gift.getPrice());
        record.setTotalDiamond(totalCost);
        record.setSessionId(request.getSessionId());
        giftRecordMapper.insert(record);

        UserBalance receiverBalance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, request.getReceiverId()));
        if (receiverBalance != null) {
            double cashReward = totalCost * 0.03;
            BigDecimal newCash = receiverBalance.getCash().add(BigDecimal.valueOf(cashReward));
            receiverBalance.setCash(newCash);
            balanceMapper.updateById(receiverBalance);

            Transaction tx = new Transaction();
            tx.setUserId(request.getReceiverId());
            tx.setType("income");
            tx.setAmount(BigDecimal.valueOf(cashReward));
            tx.setBizType("gift");
            tx.setRefId(record.getId());
            tx.setDescription("收到礼物：" + gift.getName() + " x" + quantity);
            tx.setBalanceAfter(newCash);
            transactionMapper.insert(tx);
        }

        Map<String, Object> pushData = new HashMap<>();
        pushData.put("type", "new_gift");
        pushData.put("giftName", gift.getName());
        pushData.put("giftIcon", gift.getIcon());
        pushData.put("quantity", quantity);
        pushData.put("totalDiamond", totalCost);
        pushData.put("senderId", senderId);
        ChatWebSocketHandler.sendToUser(request.getReceiverId(), JSONUtil.toJsonStr(pushData));
    }
}
