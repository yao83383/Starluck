package com.starluck.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.entity.DiamondRecord;
import com.starluck.entity.RechargeOrder;
import com.starluck.entity.Transaction;
import com.starluck.entity.UserBalance;
import com.starluck.entity.WithdrawOrder;
import com.starluck.mapper.DiamondRecordMapper;
import com.starluck.mapper.RechargeOrderMapper;
import com.starluck.mapper.TransactionMapper;
import com.starluck.mapper.UserBalanceMapper;
import com.starluck.mapper.WithdrawOrderMapper;
import com.starluck.service.PaymentService;
import com.starluck.vo.TransactionVO;
import com.starluck.vo.WalletVO;
import org.springframework.beans.factory.annotation.Value;
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
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM/dd HH:mm");

    private final UserBalanceMapper balanceMapper;
    private final RechargeOrderMapper rechargeOrderMapper;
    private final WithdrawOrderMapper withdrawOrderMapper;
    private final DiamondRecordMapper diamondRecordMapper;
    private final TransactionMapper transactionMapper;

    @Value("${app.withdraw-rate:0.06}")
    private double withdrawRate;

    public PaymentServiceImpl(UserBalanceMapper balanceMapper, RechargeOrderMapper rechargeOrderMapper,
                              WithdrawOrderMapper withdrawOrderMapper, DiamondRecordMapper diamondRecordMapper,
                              TransactionMapper transactionMapper) {
        this.balanceMapper = balanceMapper;
        this.rechargeOrderMapper = rechargeOrderMapper;
        this.withdrawOrderMapper = withdrawOrderMapper;
        this.diamondRecordMapper = diamondRecordMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public WalletVO getWallet(Long userId) {
        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));
        if (balance == null) {
            throw new BusinessException("用户余额不存在");
        }

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
                .pinkDiamonds(balance.getPinkDiamonds() != null ? balance.getPinkDiamonds() : 0)
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
        int[][] packages = {
            {6, 600}, {30, 3000}, {68, 6800}, {128, 12800}, {328, 32800}, {648, 64800}
        };
        int[] bonuses = {0, 300, 980, 2000, 5000, 10000};
        int idx = packageId.intValue() - 1;
        if (idx < 0 || idx >= packages.length) {
            throw new BusinessException("套餐不存在");
        }

        int diamondCount = packages[idx][1];
        int bonusDiamonds = bonuses[idx];
        BigDecimal amount = BigDecimal.valueOf(packages[idx][0]);

        String orderNo = "RC" + IdUtil.fastSimpleUUID().substring(0, 24).toUpperCase();
        RechargeOrder order = new RechargeOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setPackageId(packageId);
        order.setAmount(amount);
        order.setDiamonds(diamondCount);
        order.setBonusDiamonds(bonusDiamonds);
        order.setPayMethod(payMethod);
        order.setStatus("pending");
        rechargeOrderMapper.insert(order);

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

        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, order.getUserId()));
        balance.setDiamonds(balance.getDiamonds() + order.getDiamonds()
                + (order.getBonusDiamonds() == null ? 0 : order.getBonusDiamonds()));
        BigDecimal totalRecharged = balance.getTotalRecharged() == null
                ? BigDecimal.ZERO : balance.getTotalRecharged();
        balance.setTotalRecharged(totalRecharged.add(order.getAmount()));
        balanceMapper.updateById(balance);

        DiamondRecord dr = new DiamondRecord();
        dr.setUserId(order.getUserId());
        dr.setType("in");
        dr.setAmount(order.getDiamonds());
        dr.setBalanceAfter(balance.getDiamonds());
        dr.setBizType("recharge");
        dr.setRefId(order.getId());
        dr.setRemark("充值" + order.getAmount() + "元，获得" + order.getDiamonds() + "星光");
        diamondRecordMapper.insert(dr);

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
        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));
        if (balance.getIsAuthed() == null || balance.getIsAuthed() != 1) {
            throw new BusinessException("请先完成实名认证");
        }

        if (amount.compareTo(BigDecimal.TEN) < 0) {
            throw new BusinessException("单笔提现最低¥10");
        }
        if (balance.getCash().compareTo(amount) < 0) {
            throw new BusinessException("可提现余额不足");
        }

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

        balance.setCash(balance.getCash().subtract(amount));
        BigDecimal totalWithdrawn = balance.getTotalWithdrawn() == null
                ? BigDecimal.ZERO : balance.getTotalWithdrawn();
        balance.setTotalWithdrawn(totalWithdrawn.add(amount));
        balanceMapper.updateById(balance);

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal exchangePinkDiamonds(Long userId, Integer amount) {
        if (amount == null || amount <= 0) {
            throw new BusinessException("兑换数量必须大于0");
        }

        UserBalance balance = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));
        if (balance == null) {
            throw new BusinessException("用户余额不存在");
        }

        int pinkCurrent = balance.getPinkDiamonds() != null ? balance.getPinkDiamonds() : 0;
        if (pinkCurrent < amount) {
            throw new BusinessException("星尘余额不足");
        }

        BigDecimal cashIncome = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(withdrawRate))
                .setScale(2, RoundingMode.HALF_UP);

        balance.setPinkDiamonds(pinkCurrent - amount);
        BigDecimal currentCash = balance.getCash() != null ? balance.getCash() : BigDecimal.ZERO;
        balance.setCash(currentCash.add(cashIncome));
        balanceMapper.updateById(balance);

        DiamondRecord dr = new DiamondRecord();
        dr.setUserId(userId);
        dr.setType("out");
        dr.setAmount(-amount);
        dr.setBalanceAfter(balance.getPinkDiamonds());
        dr.setBizType("pink_exchange");
        dr.setRemark("兑换星尘 +" + cashIncome + "元现金");
        diamondRecordMapper.insert(dr);

        return balance.getCash();
    }
}
