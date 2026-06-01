package com.starluck.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.config.ChatWebSocketHandler;
import com.starluck.entity.*;
import com.starluck.mapper.*;
import com.starluck.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final FakeUserMapper fakeUserMapper;
    private final PushRuleMapper pushRuleMapper;
    private final PushLogMapper pushLogMapper;
    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;
    private final UserMapper userMapper;

    private static final String[] NAME_POOL = {"小诗","糖糖","梨花","若兮","茉莉","柚柚","安琪","可可","小鹿","月亮","晓雯","子萱","婉清","雨晴","南风","沐妍","栀子","清欢","一一","阿月"};
    private static final String[] CITY_POOL = {"上海","北京","深圳","广州","杭州","成都","南京","武汉","重庆","西安"};
    private static final String[] SIGN_POOL = {"寻找一个温暖的人","生活不止眼前的苟且","喜欢摄影、咖啡和旅行","期待一场认真的恋爱","喜欢看星星和电影","愿和你共度春秋","一个人也要好好生活","努力做有趣的女生"};
    private static final String[] TAG_POOL = {"旅行","咖啡","摄影","美食","音乐","电影","健身","读书","宠物","瑜伽","汉服","烹饪","逛街","插画","烘焙"};
    private static final String[] GREETINGS = {"你好呀～看你头像感觉好亲切","嗨～在干嘛呢","在吗？刚刷到你～","哈喽～感觉我们好像挺投缘的","突然好想找人聊聊天"};
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<FakeUser> getFakeUsers(String keyword) {
        LambdaQueryWrapper<FakeUser> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(FakeUser::getName, keyword).or().like(FakeUser::getCity, keyword));
        }
        wrapper.orderByDesc(FakeUser::getCreatedAt);
        return fakeUserMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FakeUser saveFakeUser(FakeUser fakeUser) {
        if (fakeUser.getId() != null) {
            fakeUserMapper.updateById(fakeUser);
        } else {
            fakeUserMapper.insert(fakeUser);
        }
        return fakeUser;
    }

    @Override
    public void deleteFakeUser(Long id) {
        fakeUserMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FakeUser> batchGenerate(int count) {
        List<FakeUser> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FakeUser f = new FakeUser();
            f.setName(NAME_POOL[RandomUtil.randomInt(NAME_POOL.length)]);
            f.setAvatarNo(RandomUtil.randomInt(1, 9));
            f.setAge(20 + RandomUtil.randomInt(10));
            f.setCity(CITY_POOL[RandomUtil.randomInt(CITY_POOL.length)]);
            f.setDist((0.5 + RandomUtil.randomDouble(5)).formatted("%.1f") + "km");
            f.setOnline(RandomUtil.randomInt(10) > 3);
            f.setVip(RandomUtil.randomInt(10) > 6);
            f.setHeight(158 + RandomUtil.randomInt(15));
            f.setWeight(42 + RandomUtil.randomInt(15));
            f.setJob("设计师");
            f.setEdu("本科");
            f.setHometown("广东深圳");
            f.setLoveStatus("单身");
            f.setSign(SIGN_POOL[RandomUtil.randomInt(SIGN_POOL.length)]);
            f.setPersona("温柔、爱笑、喜欢分享日常");
            f.setStatus(1);

            // 随机3个标签
            List<String> tags = new ArrayList<>();
            while (tags.size() < 3) {
                String t = TAG_POOL[RandomUtil.randomInt(TAG_POOL.length)];
                if (!tags.contains(t)) tags.add(t);
            }
            f.setTags(JSONUtil.toJsonStr(tags));
            list.add(f);
        }
        for (FakeUser f : list) {
            fakeUserMapper.insert(f);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualPush(Long fakeUserId, Long targetUserId) {
        FakeUser fake = fakeUserMapper.selectById(fakeUserId);
        if (fake == null) {
            throw new BusinessException("假用户不存在");
        }

        String greet = GREETINGS[RandomUtil.randomInt(GREETINGS.length)];
        String now = LocalDateTime.now().format(TIME_FMT);

        // 查找或创建会话
        ChatSession session = sessionMapper.selectOne(
                new LambdaQueryWrapper<ChatSession>()
                        .eq(ChatSession::getMaleUserId, targetUserId)
                        .eq(ChatSession::getFemaleUserId, fakeUserId));
        if (session == null) {
            session = new ChatSession();
            session.setMaleUserId(targetUserId);
            session.setFemaleUserId(fakeUserId);
            session.setIsFake(true);
            session.setType("chat");
            sessionMapper.insert(session);
        }

        // 系统提示消息
        ChatMessage sysMsg = new ChatMessage();
        sysMsg.setSessionId(session.getId());
        sysMsg.setSenderId(0L);
        sysMsg.setSenderRole("system");
        sysMsg.setMsgType("sys");
        sysMsg.setContent("TA向你发起了打招呼");
        sysMsg.setMsgTime(now);
        messageMapper.insert(sysMsg);

        // 打招呼消息
        ChatMessage msg = new ChatMessage();
        msg.setSessionId(session.getId());
        msg.setSenderId(fakeUserId);
        msg.setSenderRole("female");
        msg.setMsgType("text");
        msg.setContent(greet);
        msg.setMsgTime(now);
        messageMapper.insert(msg);

        // 更新会话
        session.setLastMsg(greet);
        session.setLastTime(now);
        session.setMaleUnread(session.getMaleUnread() + 1);
        sessionMapper.updateById(session);

        // 推送日志
        PushLog log = new PushLog();
        log.setFakeUserId(fakeUserId);
        log.setTargetUserId(targetUserId);
        log.setSource("manual");
        log.setSlot("manual");
        log.setOpened(false);
        log.setReplied(false);
        pushLogMapper.insert(log);
    }

    @Override
    public PushRule getPushRule() {
        return pushRuleMapper.selectOne(new LambdaQueryWrapper<PushRule>().last("LIMIT 1"));
    }

    @Override
    public void updatePushRule(PushRule rule) {
        if (rule.getId() != null) {
            pushRuleMapper.updateById(rule);
        } else {
            pushRuleMapper.insert(rule);
        }
    }

    @Override
    public String getAiSuggestion(Long sessionId) {
        // TODO: 对接GLM-4-Flash API（后端代理，Key存储在配置中）
        return "（AI建议功能需配置GLM API Key）";
    }
}
