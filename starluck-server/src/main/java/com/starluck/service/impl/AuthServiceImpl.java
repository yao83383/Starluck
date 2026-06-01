package com.starluck.service.impl;

import cn.hutool.core.util.IdUtil;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 认证服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String SMS_CODE_PREFIX = "sms:code:";

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserBalanceMapper userBalanceMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    private Map<String, String> codeStore;

    public AuthServiceImpl(UserMapper userMapper, UserProfileMapper userProfileMapper,
                           UserBalanceMapper userBalanceMapper, PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.userBalanceMapper = userBalanceMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void sendCode(String phone) {
        String code = "123456";
        String key = SMS_CODE_PREFIX + phone;
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(5));
        } else if (codeStore != null) {
            codeStore.put(key, code);
        }
        log.info("验证码发送成功 - phone: {}, code: {}", phone, code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO loginByPhone(LoginRequest request) {
        String phone = request.getPhone();
        String code = request.getCode();

        String key = SMS_CODE_PREFIX + phone;
        String cachedCode;
        if (redisTemplate != null) {
            cachedCode = (String) redisTemplate.opsForValue().get(key);
        } else if (codeStore != null) {
            cachedCode = codeStore.get(key);
        } else {
            cachedCode = null;
        }
        if (cachedCode == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }
        if (!cachedCode.equals(code)) {
            throw new BusinessException("验证码错误");
        }
        if (redisTemplate != null) {
            redisTemplate.delete(key);
        } else if (codeStore != null) {
            codeStore.remove(key);
        }

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null) {
            user = createNewUser(phone);
        }

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getId(), user.getPhone());

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
                .isVip(balance != null && balance.getIsVip() != null && balance.getIsVip() == 1)
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
                .isVip(balance != null && balance.getIsVip() != null && balance.getIsVip() == 1)
                .vipExpireTime(balance != null && balance.getVipExpireTime() != null
                        ? balance.getVipExpireTime().toString() : null)
                .build();
    }

    private User createNewUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setInviteCode(generateInviteCode());
        user.setStatus(1);
        user.setRole("USER");
        userMapper.insert(user);

        UserProfile profile = new UserProfile();
        profile.setUserId(user.getId());
        profile.setAvatarNo(ThreadLocalRandom.current().nextInt(1, 9));
        userProfileMapper.insert(profile);

        UserBalance balance = new UserBalance();
        balance.setUserId(user.getId());
        balance.setDiamonds(666);
        balance.setCash(BigDecimal.ZERO);
        balance.setTotalRecharged(BigDecimal.ZERO);
        balance.setTotalWithdrawn(BigDecimal.ZERO);
        balance.setIsVip(0);
        balance.setIsAuthed(0);
        balance.setDailyFreeChat(5);
        userBalanceMapper.insert(balance);

        return user;
    }

    private String generateInviteCode() {
        String code;
        do {
            code = IdUtil.fastSimpleUUID().substring(0, 8).toUpperCase();
        } while (userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getInviteCode, code)) != null);
        return code;
    }
}
