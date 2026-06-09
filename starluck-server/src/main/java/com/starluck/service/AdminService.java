package com.starluck.service;

import com.starluck.entity.FakeUser;
import com.starluck.entity.PushRule;
import com.starluck.entity.User;
import com.starluck.vo.MaleUserVO;

import java.util.List;

/**
 * 后台管理服务接口
 *
 * @author AI
 * @date 2026-06-01
 * @ai-assisted ai辅助生成,开发人员已完成审查与测试。
 */
public interface AdminService {

    /** 假用户列表（按 csOwner 过滤：null=全部, "me"=我分配的） */
    List<FakeUser> getFakeUsers(String keyword, Long csUserId);

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

    /** 获取所有客服列表 */
    List<User> getCsList();

    /** 分配假用户给客服 */
    void assignFakeToCs(Long fakeId, Long csUserId, String csName);

    /** 客服以假用户身份发送消息 */
    void sendAsCs(Long sessionId, String content);

    /** 获取真实男用户列表（含活跃会话信息） */
    List<MaleUserVO> getMaleUsers(Long csUserId);
}
