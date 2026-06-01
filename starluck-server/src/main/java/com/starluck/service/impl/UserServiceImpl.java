package com.starluck.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.entity.User;
import com.starluck.entity.UserBalance;
import com.starluck.entity.UserProfile;
import com.starluck.mapper.UserBalanceMapper;
import com.starluck.mapper.UserMapper;
import com.starluck.mapper.UserProfileMapper;
import com.starluck.service.UserService;
import com.starluck.vo.UserProfileVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 用户服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserBalanceMapper userBalanceMapper;

    public UserServiceImpl(UserMapper userMapper, UserProfileMapper userProfileMapper,
                           UserBalanceMapper userBalanceMapper) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.userBalanceMapper = userBalanceMapper;
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
}
