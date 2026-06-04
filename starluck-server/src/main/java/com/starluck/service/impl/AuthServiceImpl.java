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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 认证服务实现
 * 验证码生成策略：开发阶段随机6位数字+日志输出；生产环境接入短信SDK后将日志改为短信发送
 *
 * @author AI
 * @date 2026-06-01
 * @ai-assisted ai辅助生成,开发人员已完成审查与测试。
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final Random RANDOM = new Random();

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
    public String sendCode(String phone) {
        // 生成 6 位随机验证码
        String code = String.format("%06d", RANDOM.nextInt(1000000));
        String key = SMS_CODE_PREFIX + phone;
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(5));
        } else if (codeStore != null) {
            codeStore.put(key, code);
        }
        log.info("验证码 - phone: {}, code: {}", phone, code);
        return code;
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

        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), user.getRole());
        return buildLoginVO(user, token);
    }

    @Override
    public LoginVO loginByPassword(String phone, String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null) {
            throw new BusinessException("账号不存在，请先注册");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), user.getRole());
        return buildLoginVO(user, token);
    }

    @Override
    public LoginVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return buildLoginVO(user, null);
    }

    private LoginVO buildLoginVO(User user, String token) {
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

    private User createNewUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        // 注册时不设密码，用户可在设置中自行设置
        user.setInviteCode(generateInviteCode());
        user.setStatus(1);
        user.setRole("USER");
        userMapper.insert(user);

        UserProfile profile = new UserProfile();
        profile.setUserId(user.getId());
        profile.setAvatarNo(ThreadLocalRandom.current().nextInt(1, 9));
        profile.setGender("M"); // 默认男性，注册后引导更改
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
