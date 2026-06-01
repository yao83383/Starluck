package com.starluck.service;

import com.starluck.entity.FakeUser;
import com.starluck.entity.PushRule;

import java.util.List;

/**
 * 后台管理服务接口
 *
 * @author AI
 * @date 2026-06-01
 */
public interface AdminService {

    /** 假用户列表 */
    List<FakeUser> getFakeUsers(String keyword);

    /** 新增/更新假用户 */
    FakeUser saveFakeUser(FakeUser fakeUser);

    /** 删除假用户 */
    void deleteFakeUser(Long id);

    /** 批量生成假用户 */
    List<FakeUser> batchGenerate(int count);

    /** 手动推送 */
    void manualPush(Long fakeUserId, Long targetUserId);

    /** 获取推送规则 */
    PushRule getPushRule();

    /** 更新推送规则 */
    void updatePushRule(PushRule rule);

    /** AI建议回复 */
    String getAiSuggestion(Long sessionId);
}
