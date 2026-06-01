package com.starluck.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.dto.LoginRequest;
import com.starluck.entity.User;
import com.starluck.entity.UserBalance;
import com.starluck.entity.UserProfile;
import com.starluck.mapper.UserBalanceMapper;
import com.starluck.mapper.UserMapper;
import com.starluck.mapper.UserProfileMapper;
import com.starluck.security.JwtUtil;
import com.starluck.service.AuthService;
import com.starluck.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 认证服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserBalanceMapper userBalanceMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    @Override
    public void sendCode(String phone) {
        String code = "123456";
        // TODO: 正式环境接入短信服务（阿里云/腾讯云短信）
        // String code = RandomUtil.randomNumbers(6);
        String key = SMS_CODE_PREFIX + phone;
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(5));
        log.info("验证码发送成功 - phone: {}, code: {}", phone, code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO loginByPhone(LoginRequest request) {
        String phone = request.getPhone();
        String code = request.getCode();

        // 校验验证码
        String key = SMS_CODE_PREFIX + phone;
        String cachedCode = (String) redisTemplate.opsForValue().get(key);
        if (cachedCode == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }
        if (!cachedCode.equals(code)) {
            throw new BusinessException("验证码错误");
        }
        // 验证通过后删除验证码
        redisTemplate.delete(key);

        // 查询或创建用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        boolean isNewUser = (user == null);
        if (isNewUser) {
            user = createNewUser(phone);
        }

        // 更新登录信息
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getPhone());

        // 查询用户资料
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, user.getId()));
        UserBalance balance = userBalanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, user.getId()));

        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .phone(user.getPhone())
                .nickname(profile != null ? profile.getNickname() : null)
                .avatar(profile != null ? profile.getAvatar() : null)
                .onboarded(profile != null && profile.getNickname() != null)
                .diamonds(balance != null ? balance.getDiamonds() : 666)
                .isVip(balance != null && balance.getIsVip() == 1)
                .vipExpireTime(balance != null && balance.getVipExpireTime() != null
                        ? balance.getVipExpireTime().toString() : null)
                .build();
    }

    @Override
    public LoginVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId));
        UserBalance balance = userBalanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));

        return LoginVO.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .nickname(profile != null ? profile.getNickname() : null)
                .avatar(profile != null ? profile.getAvatar() : null)
                .onboarded(profile != null && profile.getNickname() != null)
                .diamonds(balance != null ? balance.getDiamonds() : 666)
                .isVip(balance != null && balance.getIsVip() == 1)
                .vipExpireTime(balance != null && balance.getVipExpireTime() != null
                        ? balance.getVipExpireTime().toString() : null)
                .build();
    }

    /**
     * 创建新用户
     */
    private User createNewUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setInviteCode(generateInviteCode());
        user.setStatus(1);
        user.setRole("USER");
        userMapper.insert(user);

        // 创建资料记录
        UserProfile profile = new UserProfile();
        profile.setUserId(user.getId());
        profile.setAvatarNo(RandomUtil.randomInt(1, 9));
        userProfileMapper.insert(profile);

        // 创建余额记录（新用户赠送666钻石）
        UserBalance balance = new UserBalance();
        balance.setUserId(user.getId());
        balance.setDiamonds(666);
        balance.setCash(0.00);
        balance.setDailyFreeChat(5);
        userBalanceMapper.insert(balance);

        return user;
    }

    /**
     * 生成唯一邀请码
     */
    private String generateInviteCode() {
        String code;
        do {
            code = IdUtil.fastSimpleUUID().substring(0, 8).toUpperCase();
        } while (userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getInviteCode, code)) != null);
        return code;
    }
}
