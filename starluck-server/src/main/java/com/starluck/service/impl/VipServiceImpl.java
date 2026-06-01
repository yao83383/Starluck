package com.starluck.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.entity.UserBalance;
import com.starluck.entity.VipOrder;
import com.starluck.mapper.UserBalanceMapper;
import com.starluck.mapper.VipOrderMapper;
import com.starluck.service.VipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * VIP服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Service
public class VipServiceImpl implements VipService {

    private final VipOrderMapper vipOrderMapper;
    private final UserBalanceMapper balanceMapper;

    public VipServiceImpl(VipOrderMapper vipOrderMapper, UserBalanceMapper balanceMapper) {
        this.vipOrderMapper = vipOrderMapper;
        this.balanceMapper = balanceMapper;
    }

    @Override
    public Map<String, Object> getPlans() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("month", Map.of("name", "月卡", "price", 30, "originalPrice", 30, "tag", ""));
        result.put("quarter", Map.of("name", "季卡", "price", 68, "originalPrice", 135, "tag", "限时5折"));
        result.put("year", Map.of("name", "年卡", "price", 198, "originalPrice", 360, "tag", ""));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subscribe(Long userId, String planType, String payMethod) {
        BigDecimal amount;
        int months;
        switch (planType) {
            case "month":
                amount = BigDecimal.valueOf(30);
                months = 1;
                break;
            case "quarter":
                amount = BigDecimal.valueOf(68);
                months = 3;
                break;
            case "year":
                amount = BigDecimal.valueOf(198);
                months = 12;
                break;
            default:
                throw new BusinessException("不支持的套餐类型");
        }

        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));

        String orderNo = "VIP" + IdUtil.fastSimpleUUID().substring(0, 24).toUpperCase();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMonths(months);

        if (balance.getIsVip() != null && balance.getIsVip() == 1
                && balance.getVipExpireTime() != null
                && balance.getVipExpireTime().isAfter(now)) {
            expireTime = balance.getVipExpireTime().plusMonths(months);
        }

        VipOrder order = new VipOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setPlanType(planType);
        order.setAmount(amount);
        order.setPayMethod(payMethod);
        order.setStatus("paid");
        order.setTradeNo("DEMO_" + IdUtil.fastSimpleUUID().substring(0, 16));
        order.setStartTime(now);
        order.setExpireTime(expireTime);
        order.setPaidAt(now);
        vipOrderMapper.insert(order);

        balance.setIsVip(1);
        balance.setVipExpireTime(expireTime);
        balance.setDailyFreeChat(100);
        balanceMapper.updateById(balance);
    }
}
