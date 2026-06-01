package com.starluck.service;

import com.starluck.entity.UserProfile;
import com.starluck.vo.UserProfileVO;

/**
 * 用户服务接口
 *
 * @author AI
 * @date 2026-06-01
 */
public interface UserService {

    /**
     * 获取用户完整资料
     */
    UserProfileVO getUserProfile(Long userId);

    /**
     * 更新用户资料
     */
    void updateProfile(Long userId, UserProfile profile);
}
