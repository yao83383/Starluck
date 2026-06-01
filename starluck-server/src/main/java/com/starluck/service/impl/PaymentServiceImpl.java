package com.starluck.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.entity.*;
import com.starluck.mapper.*;
import com.starluck.service.PaymentService;
import com.starluck.vo.TransactionVO;
import com.starluck.vo.WalletVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserBalanceMapper balanceMapper;
    private final RechargeOrderMapper rechargeOrderMapper;
    private final WithdrawOrderMapper withdrawOrderMapper;
    private final DiamondRecordMapper diamondRecordMapper;
    private final TransactionMapper transactionMapper;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM/dd HH:mm");

    @Override
    public WalletVO getWallet(Long userId) {
        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));
        if (balance == null) {
            throw new BusinessException("用户余额不存在");
        }

        // 本月交易记录
        List<Transaction> txs = transactionMapper.selectList(
                new LambdaQueryWrapper<Transaction>()
                        .eq(Transaction::getUserId, userId)
                        .orderByDesc(Transaction::getCreatedAt)
                        .last("LIMIT 20"));

        List<TransactionVO> txVOs = txs.stream().map(tx -> TransactionVO.builder()
                .id(tx.getId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .bizType(tx.getBizType())
                .description(tx.getDescription())
                .createdAt(tx.getCreatedAt() != null ? tx.getCreatedAt().format(DATE_FMT) : null)
                .build()).collect(Collectors.toList());

        return WalletVO.builder()
                .diamonds(balance.getDiamonds())
                .cash(balance.getCash())
                .monthGiftIncome(BigDecimal.ZERO)
                .monthChatIncome(BigDecimal.ZERO)
                .monthCallIncome(BigDecimal.ZERO)
                .recentTransactions(txVOs)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRechargeOrder(Long userId, Long packageId, String payMethod) {
        // 充值套餐定义（前端目前也有一套，为简化这里硬编码）
        int[][] packages = {
            {60, 600}, {300, 3000}, {680, 6800}, {1280, 12800}, {3280, 32800}, {6480, 64800}
        };
        int idx = packageId.intValue() - 1;
        if (idx < 0 || idx >= packages.length) {
            throw new BusinessException("套餐不存在");
        }

        int diamondCount = packages[idx][1];
        BigDecimal amount = BigDecimal.valueOf(packages[idx][0]);

        String orderNo = "RC" + IdUtil.fastSimpleUUID().substring(0, 24).toUpperCase();
        RechargeOrder order = new RechargeOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setPackageId(packageId);
        order.setAmount(amount);
        order.setDiamonds(diamondCount);
        order.setBonusDiamonds(0);
        order.setPayMethod(payMethod);
        order.setStatus("pending");
        rechargeOrderMapper.insert(order);

        // TODO: 正式环境调用微信/支付宝下单接口，返回支付参数
        // 演示环境直接完成支付
        handlePayCallback(orderNo, "DEMO_" + IdUtil.fastSimpleUUID().substring(0, 16));
        return orderNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePayCallback(String orderNo, String tradeNo) {
        RechargeOrder order = rechargeOrderMapper.selectOne(
                new LambdaQueryWrapper<RechargeOrder>().eq(RechargeOrder::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!"pending".equals(order.getStatus())) {
            return;
        }

        order.setStatus("paid");
        order.setTradeNo(tradeNo);
        order.setPaidAt(LocalDateTime.now());
        rechargeOrderMapper.updateById(order);

        // 增加钻石
        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, order.getUserId()));
        balance.setDiamonds(balance.getDiamonds() + order.getDiamonds() + order.getBonusDiamonds());
        balance.setTotalRecharged(balance.getTotalRecharged().add(order.getAmount()));
        balanceMapper.updateById(balance);

        // 钻石流水
        DiamondRecord dr = new DiamondRecord();
        dr.setUserId(order.getUserId());
        dr.setType("in");
        dr.setAmount(order.getDiamonds());
        dr.setBalanceAfter(balance.getDiamonds());
        dr.setBizType("recharge");
        dr.setRefId(order.getId());
        dr.setRemark("充值" + order.getAmount() + "元，获得" + order.getDiamonds() + "钻石");
        diamondRecordMapper.insert(dr);

        // 交易流水
        Transaction tx = new Transaction();
        tx.setUserId(order.getUserId());
        tx.setType("expense");
        tx.setAmount(order.getAmount());
        tx.setBizType("recharge");
        tx.setRefId(order.getId());
        tx.setDescription("充值" + order.getAmount() + "元");
        tx.setBalanceAfter(balance.getCash());
        transactionMapper.insert(tx);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyWithdraw(Long userId, BigDecimal amount, String method, String account) {
        // 校验实名
        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));
        if (balance.getIsAuthed() != 1) {
            throw new BusinessException("请先完成实名认证");
        }

        if (amount.compareTo(BigDecimal.TEN) < 0) {
            throw new BusinessException("单笔提现最低¥10");
        }
        if (balance.getCash().compareTo(amount) < 0) {
            throw new BusinessException("可提现余额不足");
        }

        // 手续费
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.006)).setScale(2, RoundingMode.HALF_UP);
        if (fee.compareTo(BigDecimal.ONE) < 0) {
            fee = BigDecimal.ONE;
        }
        BigDecimal actual = amount.subtract(fee);

        String orderNo = "WD" + IdUtil.fastSimpleUUID().substring(0, 24).toUpperCase();
        WithdrawOrder order = new WithdrawOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setAmount(amount);
        order.setFee(fee);
        order.setActualAmount(actual);
        order.setMethod(method);
        order.setAccount(account);
        order.setStatus("pending");
        withdrawOrderMapper.insert(order);

        // 冻结提现余额
        balance.setCash(balance.getCash().subtract(amount));
        balance.setTotalWithdrawn(balance.getTotalWithdrawn().add(amount));
        balanceMapper.updateById(balance);

        // 交易流水
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setType("expense");
        tx.setAmount(amount);
        tx.setBizType("withdraw");
        tx.setRefId(order.getId());
        tx.setDescription("提现" + amount + "元，手续费" + fee + "元");
        tx.setBalanceAfter(balance.getCash());
        transactionMapper.insert(tx);
    }
}
