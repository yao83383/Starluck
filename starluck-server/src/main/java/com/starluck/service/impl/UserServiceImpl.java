package com.starluck.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.entity.FakeUser;
import com.starluck.entity.User;
import com.starluck.entity.UserBalance;
import com.starluck.entity.UserProfile;
import com.starluck.mapper.FakeUserMapper;
import com.starluck.mapper.UserBalanceMapper;
import com.starluck.mapper.UserMapper;
import com.starluck.mapper.UserProfileMapper;
import com.starluck.service.UserService;
import com.starluck.vo.UserCardVO;
import com.starluck.vo.UserProfileVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 *
 * @author AI
 * @date 2026-06-01
 * @ai-assisted ai辅助生成,开发人员已完成审查与测试。
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserBalanceMapper userBalanceMapper;
    private final FakeUserMapper fakeUserMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, UserProfileMapper userProfileMapper,
                           UserBalanceMapper userBalanceMapper, FakeUserMapper fakeUserMapper,
                           PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.userBalanceMapper = userBalanceMapper;
        this.fakeUserMapper = fakeUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserProfileVO getUserProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId));
        if (profile == null) {
            throw new BusinessException("用户资料不存在");
        }
        UserBalance balance = userBalanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, userId));

        List<String> tags = Collections.emptyList();
        if (StrUtil.isNotBlank(profile.getTags())) {
            try {
                tags = JSONUtil.toList(profile.getTags(), String.class);
            } catch (Exception ignored) {}
        }

        return UserProfileVO.builder()
                .userId(user.getId())
                .nickname(profile.getNickname())
                .avatar(profile.getAvatar())
                .avatarNo(profile.getAvatarNo())
                .phone(user.getPhone())
                .gender(profile.getGender())
                .age(profile.getAge())
                .city(profile.getCity())
                .height(profile.getHeight())
                .weight(profile.getWeight())
                .job(profile.getJob())
                .edu(profile.getEdu())
                .hometown(profile.getHometown())
                .loveStatus(profile.getLoveStatus())
                .sign(profile.getSign())
                .tags(tags)
                .inviteCode(user.getInviteCode())
                .diamonds(balance != null ? balance.getDiamonds() : 0)
                .cash(balance != null ? balance.getCash() : BigDecimal.ZERO)
                .isVip(balance != null && balance.getIsVip() != null && balance.getIsVip() == 1)
                .vipExpireTime(balance != null && balance.getVipExpireTime() != null
                        ? balance.getVipExpireTime().toString() : null)
                .isAuthed(balance != null && balance.getIsAuthed() != null && balance.getIsAuthed() == 1)
                .fansCount(profile.getFansCount())
                .followCount(profile.getFollowCount())
                .friendsCount(profile.getFriendsCount())
                .likesCount(profile.getLikesCount())
                .build();
    }

    @Override
    public void updateProfile(Long userId, UserProfile profile) {
        UserProfile existing = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId));
        if (existing == null) {
            throw new BusinessException("用户资料不存在");
        }
        profile.setId(existing.getId());
        profile.setUserId(userId);
        userProfileMapper.updateById(profile);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (StrUtil.isBlank(newPassword) || newPassword.length() < 6) {
            throw new BusinessException("密码长度不能少于6位");
        }
        boolean hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();
        if (hasPassword && StrUtil.isBlank(oldPassword)) {
            throw new BusinessException("已设置过密码，请输入旧密码");
        }
        if (hasPassword && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    @Override
    public List<UserCardVO> getDiscoverList(Long currentUserId) {
        // 1. 获取当前用户性别
        UserProfile selfProfile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, currentUserId));
        String selfGender = selfProfile != null ? selfProfile.getGender() : null;

        List<UserCardVO> result = new ArrayList<>();

        // 2. 真实用户：排除自己 + 仅异性
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<User>()
                .ne(User::getId, currentUserId)
                .eq(User::getStatus, 1)
                .orderByDesc(User::getCreatedAt)
                .last("LIMIT 50");
        List<User> users = userMapper.selectList(userQuery);

        if (!users.isEmpty()) {
            List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
            LambdaQueryWrapper<UserProfile> profileQuery = new LambdaQueryWrapper<UserProfile>()
                    .in(UserProfile::getUserId, userIds);
            if (StrUtil.isNotBlank(selfGender)) {
                String oppositeGender = "M".equals(selfGender) ? "F" : "M";
                profileQuery.eq(UserProfile::getGender, oppositeGender);
            }
            List<UserProfile> profiles = userProfileMapper.selectList(profileQuery);

            users.stream()
                    .filter(u -> profiles.stream().anyMatch(pr -> pr.getUserId().equals(u.getId())))
                    .forEach(u -> {
                        UserProfile p = profiles.stream().filter(pr -> pr.getUserId().equals(u.getId())).findFirst().orElse(null);
                        List<String> tags = Collections.emptyList();
                        if (p != null && StrUtil.isNotBlank(p.getTags())) {
                            try { tags = JSONUtil.toList(p.getTags(), String.class); } catch (Exception e) {}
                        }
                        result.add(UserCardVO.build(
                                u.getId(),
                                p != null && StrUtil.isNotBlank(p.getNickname()) ? p.getNickname() : "神秘星人",
                                p != null ? p.getAvatarNo() : 1,
                                p != null ? p.getGender() : null,
                                p != null && p.getAge() != null ? p.getAge() : 20,
                                p != null && StrUtil.isNotBlank(p.getCity()) ? p.getCity() : "未设置",
                                p != null ? p.getSign() : "",
                                tags
                        ));
                    });
        }

        // 3. 假用户：默认女性，仅向男用户展示（或当前用户未设置性别时也展示）
        // 假用户 ID 用 9000+ 前缀避免与真实用户 ID 冲突（前端通过 isFake 标识）
        if (selfGender == null || "M".equals(selfGender)) {
            List<FakeUser> fakes = fakeUserMapper.selectList(
                    new LambdaQueryWrapper<FakeUser>()
                            .eq(FakeUser::getStatus, 1)
                            .orderByDesc(FakeUser::getCreatedAt)
                            .last("LIMIT 30"));
            for (FakeUser f : fakes) {
                result.add(UserCardVO.fromFake(f));
            }
        }

        // 4. 打乱顺序，让真实用户和假用户交错展示
        Collections.shuffle(result);
        return result;
    }
}
