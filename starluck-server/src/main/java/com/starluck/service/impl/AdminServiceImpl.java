package com.starluck.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.client.GlmClient;
import com.starluck.common.BusinessException;
import com.starluck.entity.ChatMessage;
import com.starluck.entity.ChatSession;
import com.starluck.entity.FakeUser;
import com.starluck.entity.PushLog;
import com.starluck.entity.PushRule;
import com.starluck.entity.User;
import com.starluck.mapper.ChatMessageMapper;
import com.starluck.mapper.ChatSessionMapper;
import com.starluck.mapper.FakeUserMapper;
import com.starluck.mapper.PushLogMapper;
import com.starluck.mapper.PushRuleMapper;
import com.starluck.mapper.UserMapper;
import com.starluck.mapper.UserProfileMapper;
import com.starluck.entity.UserProfile;
import com.starluck.service.AdminService;
import com.starluck.vo.AdminSessionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 后台管理服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Service
public class AdminServiceImpl implements AdminService {

    private static final String[] NAME_POOL = {"小雅","糖心","月瑶","星梦","若水","语嫣","柔柔","楚楚","暖暖","桃桃","露露","茉茉","依依","晴儿","雪儿","璃璃","芸芸","莲莲","琪琪","妮妮","小诗","糖糖","梨花","若兮","茉莉","柚柚","安琪","可可","小鹿","月亮","晓雯","子萱","婉清","雨晴","南风","沐妍","栀子","清欢","一一","阿月"};
    private static final String[] CITY_POOL = {"上海","北京","深圳","广州","杭州","成都","南京","武汉","重庆","西安"};
    private static final String[] SIGN_POOL = {"寻找一个温暖的人","生活不止眼前的苟且","喜欢摄影、咖啡和旅行","期待一场认真的恋爱","喜欢看星星和电影","愿和你共度春秋","一个人也要好好生活","努力做有趣的女生"};
    private static final String[] TAG_POOL = {"旅行","咖啡","摄影","美食","音乐","电影","健身","读书","宠物","瑜伽","汉服","烹饪","逛街","插画","烘焙"};
    private static final String[] GREETINGS = {"你好呀～看你头像感觉好亲切","嗨～在干嘛呢","在吗？刚刷到你～","哈喽～感觉我们好像挺投缘的","突然好想找人聊聊天"};
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final FakeUserMapper fakeUserMapper;
    private final PushRuleMapper pushRuleMapper;
    private final PushLogMapper pushLogMapper;
    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final GlmClient glmClient;

    public AdminServiceImpl(FakeUserMapper fakeUserMapper, PushRuleMapper pushRuleMapper,
                            PushLogMapper pushLogMapper, ChatSessionMapper sessionMapper,
                            ChatMessageMapper messageMapper, UserMapper userMapper,
                            UserProfileMapper userProfileMapper, GlmClient glmClient) {
        this.fakeUserMapper = fakeUserMapper;
        this.pushRuleMapper = pushRuleMapper;
        this.pushLogMapper = pushLogMapper;
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.glmClient = glmClient;
    }

    @Override
    public List<FakeUser> getFakeUsers(String keyword, Long csUserId) {
        LambdaQueryWrapper<FakeUser> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(FakeUser::getName, keyword).or().like(FakeUser::getCity, keyword));
        }
        // CS 角色只看到分配给自己的假用户
        if (csUserId != null) {
            wrapper.eq(FakeUser::getCsOwner, String.valueOf(csUserId));
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
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        List<FakeUser> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FakeUser f = new FakeUser();
            f.setName(NAME_POOL[rnd.nextInt(NAME_POOL.length)]);
            f.setAvatarNo(rnd.nextInt(1, 9));
            f.setAge(20 + rnd.nextInt(10));
            f.setCity(CITY_POOL[rnd.nextInt(CITY_POOL.length)]);
            f.setDist(String.format("%.1f", 0.5 + rnd.nextDouble() * 5) + "km");
            f.setOnline(rnd.nextInt(10) > 3);
            f.setVip(rnd.nextInt(10) > 6);
            f.setHeight(158 + rnd.nextInt(15));
            f.setWeight(42 + rnd.nextInt(15));
            f.setJob("设计师");
            f.setEdu("本科");
            f.setHometown("广东深圳");
            f.setLoveStatus("单身");
            f.setSign(SIGN_POOL[rnd.nextInt(SIGN_POOL.length)]);
            f.setPersona("温柔、爱笑、喜欢分享日常");
            f.setStatus(1);
            // 对齐真用户的新字段
            f.setPhone("199" + String.format("%08d", 100000 + System.currentTimeMillis() % 900000));
            f.setGender("F");
            f.setIsAuthed(1);
            f.setDiamonds(666);
            f.setCash(java.math.BigDecimal.ZERO);
            f.setUserType("FAKE");

            List<String> tags = new ArrayList<>();
            while (tags.size() < 3) {
                String t = TAG_POOL[rnd.nextInt(TAG_POOL.length)];
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

        String greet = GREETINGS[ThreadLocalRandom.current().nextInt(GREETINGS.length)];
        String now = LocalDateTime.now().format(TIME_FMT);

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
            session.setMaleUnread(0);
            session.setFemaleUnread(0);
            sessionMapper.insert(session);
        }

        ChatMessage sysMsg = new ChatMessage();
        sysMsg.setSessionId(session.getId());
        sysMsg.setSenderId(0L);
        sysMsg.setSenderRole("system");
        sysMsg.setMsgType("sys");
        sysMsg.setContent("TA向你发起了打招呼");
        sysMsg.setMsgTime(now);
        messageMapper.insert(sysMsg);

        ChatMessage msg = new ChatMessage();
        msg.setSessionId(session.getId());
        msg.setSenderId(fakeUserId);
        msg.setSenderRole("female");
        msg.setMsgType("text");
        msg.setContent(greet);
        msg.setMsgTime(now);
        messageMapper.insert(msg);

        session.setLastMsg(greet);
        session.setLastTime(now);
        session.setMaleUnread((session.getMaleUnread() == null ? 0 : session.getMaleUnread()) + 1);
        sessionMapper.updateById(session);

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
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) {
            throw new BusinessException("会话不存在");
        }

        Long fakeUserId = session.getFemaleUserId();
        Long maleUserId = session.getMaleUserId();

        FakeUser fake = fakeUserMapper.selectById(fakeUserId);
        if (fake == null) {
            throw new BusinessException("假用户不存在");
        }

        List<ChatMessage> msgs = messageMapper.selectList(
                new LambdaQueryWrapper<ChatMessage>()
                        .eq(ChatMessage::getSessionId, sessionId)
                        .eq(ChatMessage::getMsgType, "text")
                        .orderByDesc(ChatMessage::getId)
                        .last("LIMIT 6"));
        Collections.reverse(msgs);

        String tags = "";
        if (fake.getTags() != null && !fake.getTags().isEmpty()) {
            try {
                tags = JSONUtil.toJsonStr(JSONUtil.parseArray(fake.getTags()));
                tags = tags.replace("[", "").replace("]", "").replace("\"", "");
            } catch (Exception ignore) {
                tags = fake.getTags();
            }
        }

        String systemPrompt = String.format(
                "你是%s，%d岁女孩，%s人。性格：%s。签名：%s。兴趣：%s。" +
                "你正在和刚认识的男生聊天。回复要求：像真人微信聊天，尽量短，通常1句话，最多不超过15个字。" +
                "语气轻松自然，可以适当加1个emoji。不主动提钱和礼物。不用每句都带问句。",
                fake.getName(), fake.getAge(), fake.getCity(),
                fake.getPersona() != null ? fake.getPersona() : "温柔、爱笑",
                fake.getSign() != null ? fake.getSign() : "",
                tags
        );

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> sysMsg = new HashMap<>();
        sysMsg.put("role", "system");
        sysMsg.put("content", systemPrompt);
        messages.add(sysMsg);

        for (ChatMessage msg : msgs) {
            Map<String, String> m = new HashMap<>();
            if (msg.getSenderId().equals(maleUserId)) {
                m.put("role", "user");
            } else if (msg.getSenderId().equals(fakeUserId)) {
                m.put("role", "assistant");
            } else {
                continue;
            }
            m.put("content", msg.getContent());
            messages.add(m);
        }

        boolean hasUserMsg = false;
        for (Map<String, String> m : messages) {
            if ("user".equals(m.get("role"))) {
                hasUserMsg = true;
                break;
            }
        }
        if (!hasUserMsg) {
            Map<String, String> m = new HashMap<>();
            m.put("role", "user");
            m.put("content", "（对方刚刚向你打招呼，请友好回复）");
            messages.add(m);
        }

        return glmClient.chat(messages);
    }

    @Override
    public List<User> getCsList() {
        return userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .eq(User::getRole, "CS")
                        .eq(User::getStatus, 1)
                        .orderByAsc(User::getId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignFakeToCs(Long fakeId, Long csUserId, String csName) {
        FakeUser fake = fakeUserMapper.selectById(fakeId);
        if (fake == null) throw new BusinessException("假用户不存在");
        fake.setCsOwner(csUserId != null ? String.valueOf(csUserId) : null);
        fakeUserMapper.updateById(fake);
    }

    @Override
    public List<AdminSessionVO> getAdminSessions(Long csUserId) {
        List<ChatSession> sessions = sessionMapper.selectList(
                new LambdaQueryWrapper<ChatSession>()
                        .eq(ChatSession::getIsFake, true)
                        .orderByDesc(ChatSession::getUpdatedAt));

        List<Long> fakeIds = sessions.stream().map(ChatSession::getFemaleUserId).distinct().collect(java.util.stream.Collectors.toList());
        List<FakeUser> fakes = fakeIds.isEmpty() ? Collections.emptyList() :
                fakeUserMapper.selectBatchIds(fakeIds);
        Map<Long, FakeUser> fakeMap = fakes.stream().collect(java.util.stream.Collectors.toMap(FakeUser::getId, f -> f, (a, b) -> a));

        if (csUserId != null) {
            String csStr = String.valueOf(csUserId);
            sessions = sessions.stream().filter(s -> {
                FakeUser f = fakeMap.get(s.getFemaleUserId());
                return f != null && csStr.equals(f.getCsOwner());
            }).collect(java.util.stream.Collectors.toList());
        }

        List<Long> maleIds = sessions.stream().map(ChatSession::getMaleUserId).distinct().collect(java.util.stream.Collectors.toList());
        List<User> males = maleIds.isEmpty() ? Collections.emptyList() : userMapper.selectBatchIds(maleIds);
        Map<Long, User> maleMap = males.stream().collect(java.util.stream.Collectors.toMap(User::getId, u -> u, (a, b) -> a));

        List<UserProfile> profiles = maleIds.isEmpty() ? Collections.emptyList() :
                userProfileMapper.selectList(new LambdaQueryWrapper<UserProfile>().in(UserProfile::getUserId, maleIds));
        Map<Long, UserProfile> profileMap = profiles.stream().collect(java.util.stream.Collectors.toMap(UserProfile::getUserId, p -> p, (a, b) -> a));

        List<AdminSessionVO> result = new ArrayList<>();
        for (ChatSession s : sessions) {
            FakeUser fake = fakeMap.get(s.getFemaleUserId());
            User male = maleMap.get(s.getMaleUserId());
            UserProfile profile = profileMap.get(s.getMaleUserId());

            result.add(AdminSessionVO.builder()
                    .sessionId(s.getId())
                    .maleId(s.getMaleUserId())
                    .maleName(profile != null && profile.getNickname() != null ? profile.getNickname() : (male != null ? male.getPhone() : "未知"))
                    .malePhone(male != null ? male.getPhone() : "")
                    .maleAv(profile != null ? profile.getAvatarNo() : 1)
                    .femaleId(s.getFemaleUserId())
                    .femaleName(fake != null ? fake.getName() : "已删除")
                    .femaleAv(fake != null ? fake.getAvatarNo() : 1)
                    .lastMsg(s.getLastMsg())
                    .lastTime(s.getLastTime())
                    .maleUnread(s.getMaleUnread() != null ? s.getMaleUnread() : 0)
                    .femaleUnread(s.getFemaleUnread() != null ? s.getFemaleUnread() : 0)
                    .build());
        }
        return result;
    }

    @Override
    public void sendAsCs(Long sessionId, String content) {
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) throw new BusinessException("会话不存在");

        Long fakeUserId = session.getFemaleUserId();
        Long maleUserId = session.getMaleUserId();
        String now = LocalDateTime.now().format(TIME_FMT);

        ChatMessage msg = new ChatMessage();
        msg.setSessionId(sessionId);
        msg.setSenderId(fakeUserId);
        msg.setSenderRole("female");
        msg.setMsgType("text");
        msg.setContent(content);
        msg.setMsgTime(now);
        msg.setIsRead(0);
        msg.setReplied(0);
        messageMapper.insert(msg);

        session.setLastMsg(content);
        session.setLastTime(now);
        session.setMaleUnread((session.getMaleUnread() == null ? 0 : session.getMaleUnread()) + 1);
        sessionMapper.updateById(session);

        Map<String, Object> pushData = new HashMap<>();
        pushData.put("type", "new_message");
        pushData.put("sessionId", sessionId);
        pushData.put("content", content);
        pushData.put("senderId", fakeUserId);
        com.starluck.config.ChatWebSocketHandler.sendToUser(maleUserId, JSONUtil.toJsonStr(pushData));
    }
}
