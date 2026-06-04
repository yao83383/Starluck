package com.starluck.service;

import com.starluck.entity.UserProfile;
import com.starluck.vo.UserCardVO;
import com.starluck.vo.UserProfileVO;

import java.util.List;

public interface UserService {

    UserProfileVO getUserProfile(Long userId);

    void updateProfile(Long userId, UserProfile profile);

    /**
     * 获取发现页用户列表
     * @param currentUserId 当前用户ID（排除自己）
     * @return 用户卡片列表
     */
    List<UserCardVO> getDiscoverList(Long currentUserId);

    /**
     * 设置/修改登录密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword);
}
