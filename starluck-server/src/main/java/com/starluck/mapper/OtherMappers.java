package com.starluck.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starluck.entity.Gift;
import com.starluck.entity.GiftRecord;
import com.starluck.entity.DiamondRecord;
import com.starluck.entity.RechargeOrder;
import com.starluck.entity.WithdrawOrder;
import com.starluck.entity.VipOrder;
import com.starluck.entity.InviteRecord;
import com.starluck.entity.PushRule;
import com.starluck.entity.PushLog;
import com.starluck.entity.RealNameAuth;
import com.starluck.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper public interface GiftMapper extends BaseMapper<Gift> {}
@Mapper public interface GiftRecordMapper extends BaseMapper<GiftRecord> {}
@Mapper public interface DiamondRecordMapper extends BaseMapper<DiamondRecord> {}
@Mapper public interface RechargeOrderMapper extends BaseMapper<RechargeOrder> {}
@Mapper public interface WithdrawOrderMapper extends BaseMapper<WithdrawOrder> {}
@Mapper public interface VipOrderMapper extends BaseMapper<VipOrder> {}
@Mapper public interface InviteRecordMapper extends BaseMapper<InviteRecord> {}
@Mapper public interface PushRuleMapper extends BaseMapper<PushRule> {}
@Mapper public interface PushLogMapper extends BaseMapper<PushLog> {}
@Mapper public interface RealNameAuthMapper extends BaseMapper<RealNameAuth> {}
@Mapper public interface TransactionMapper extends BaseMapper<Transaction> {}
