package com.starluck.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.config.ChatWebSocketHandler;
import com.starluck.dto.ChatSendRequest;
import com.starluck.entity.ChatMessage;
import com.starluck.entity.ChatSession;
import com.starluck.entity.DiamondRecord;
import com.starluck.entity.FakeUser;
import com.starluck.entity.UserBalance;
import com.starluck.entity.UserProfile;
import com.starluck.mapper.ChatMessageMapper;
import com.starluck.mapper.ChatSessionMapper;
import com.starluck.mapper.DiamondRecordMapper;
import com.starluck.mapper.FakeUserMapper;
import com.starluck.mapper.UserBalanceMapper;
import com.starluck.mapper.UserProfileMapper;
import com.starluck.service.ChatService;
import com.starluck.vo.ChatMessageVO;
import com.starluck.vo.ChatSessionVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 聊天服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Service
public class ChatServiceImpl implements ChatService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;
    private final UserBalanceMapper balanceMapper;
    private final DiamondRecordMapper diamondRecordMapper;
    private final FakeUserMapper fakeUserMapper;
    private final UserProfileMapper userProfileMapper;

    @Value("${app.chat-price:30}")
    private int chatPrice;

    @Value("${app.pink-diamond-reward:10}")
    private int pinkDiamondReward;

    public ChatServiceImpl(ChatSessionMapper sessionMapper, ChatMessageMapper messageMapper,
                           UserBalanceMapper balanceMapper, DiamondRecordMapper diamondRecordMapper,
                           FakeUserMapper fakeUserMapper, UserProfileMapper userProfileMapper) {
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
        this.balanceMapper = balanceMapper;
        this.diamondRecordMapper = diamondRecordMapper;
        this.fakeUserMapper = fakeUserMapper;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public List<ChatSessionVO> getSessionList(Long userId) {
        List<ChatSession> sessions = sessionMapper.selectList(
                new LambdaQueryWrapper<ChatSession>()
                        .eq(ChatSession::getMaleUserId, userId)
                        .or().eq(ChatSession::getFemaleUserId, userId)
                        .orderByDesc(ChatSession::getUpdatedAt));

        return sessions.stream().map(s -> {
            boolean isMale = s.getMaleUserId().equals(userId);
            Long peerId = isMale ? s.getFemaleUserId() : s.getMaleUserId();
            int unread = isMale ? (s.getMaleUnread() == null ? 0 : s.getMaleUnread())
                                : (s.getFemaleUnread() == null ? 0 : s.getFemaleUnread());

            String peerName = "未知";
            Integer peerAv = 1;
            String peerCity = "";
            boolean peerOnline = false;
            boolean peerVip = false;

            if (s.getIsFake() != null && s.getIsFake()) {
                FakeUser fake = fakeUserMapper.selectById(peerId);
                if (fake != null) {
                    peerName = fake.getName();
                    peerAv = fake.getAvatarNo();
                    peerCity = fake.getCity();
                    peerOnline = fake.getOnline() != null && fake.getOnline();
                    peerVip = fake.getVip() != null && fake.getVip();
                }
            } else {
                UserProfile profile = userProfileMapper.selectOne(
                        new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, peerId));
                if (profile != null) {
                    peerName = profile.getNickname();
                    peerAv = profile.getAvatarNo();
                    peerCity = profile.getCity();
                }
            }

            return ChatSessionVO.builder()
                    .sessionId(s.getId())
                    .peerUserId(peerId)
                    .peerName(peerName)
                    .peerAvatarNo(peerAv)
                    .peerCity(peerCity)
                    .peerOnline(peerOnline)
                    .peerVip(peerVip)
                    .isFake(s.getIsFake())
                    .lastMsg(s.getLastMsg())
                    .lastTime(s.getLastTime())
                    .unread(unread)
                    .type(s.getType())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageVO> getMessages(Long sessionId, Long userId) {
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) {
            throw new BusinessException("会话不存在");
        }

        // 将对方发给我的未读消息标记为已读
        Long otherUserId = session.getMaleUserId().equals(userId) ? session.getFemaleUserId() : session.getMaleUserId();
        messageMapper.markAsRead(sessionId, otherUserId);

        // 更新当前用户未读数为0
        if (session.getMaleUserId().equals(userId)) {
            session.setMaleUnread(0);
        } else {
            session.setFemaleUnread(0);
        }
        sessionMapper.updateById(session);

        List<ChatMessage> messages = messageMapper.selectList(
                new LambdaQueryWrapper<ChatMessage>()
                        .eq(ChatMessage::getSessionId, sessionId)
                        .orderByAsc(ChatMessage::getCreatedAt));

        return messages.stream().map(m -> ChatMessageVO.builder()
                .id(m.getId())
                .sessionId(m.getSessionId())
                .senderId(m.getSenderId())
                .senderRole(m.getSenderRole())
                .msgType(m.getMsgType())
                .content(m.getContent())
                .isRead(m.getIsRead() != null ? m.getIsRead() : 0)
                .giftEmoji(m.getGiftEmoji())
                .giftName(m.getGiftName())
                .costDiamond(m.getCostDiamond())
                .msgTime(m.getMsgTime())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessageVO sendMessage(Long senderId, ChatSendRequest request) {
        ChatSession session = sessionMapper.selectById(request.getSessionId());
        if (session == null) {
            throw new BusinessException("会话不存在");
        }

        boolean isMale = session.getMaleUserId().equals(senderId);
        Long receiverId = isMale ? session.getFemaleUserId() : session.getMaleUserId();
        String senderRole = isMale ? "male" : "female";

        String now = LocalDateTime.now().format(TIME_FMT);
        int costDiamond = 0;
        int pinkEarned = 0;

        boolean isRealSession = !Boolean.TRUE.equals(session.getIsFake());

        if (isRealSession) {
            UserBalance senderBalance = balanceMapper.selectOne(
                    new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, senderId));
            if (senderBalance == null || senderBalance.getDiamonds() < chatPrice) {
                throw new BusinessException("星光余额不足，请充值");
            }

            boolean hasFreeChat = senderBalance.getIsVip() != null && senderBalance.getIsVip() == 1
                    && senderBalance.getDailyFreeChat() != null && senderBalance.getDailyFreeChat() > 0;

            if (hasFreeChat) {
                senderBalance.setDailyFreeChat(senderBalance.getDailyFreeChat() - 1);
            } else {
                costDiamond = chatPrice;
                senderBalance.setDiamonds(senderBalance.getDiamonds() - chatPrice);

                DiamondRecord dr = new DiamondRecord();
                dr.setUserId(senderId);
                dr.setType("out");
                dr.setAmount(-chatPrice);
                dr.setBalanceAfter(senderBalance.getDiamonds());
                dr.setBizType("chat");
                dr.setRemark("聊天扣费·星光");
                diamondRecordMapper.insert(dr);
            }

            ChatMessage lastMsg = messageMapper.selectOne(
                    new LambdaQueryWrapper<ChatMessage>()
                            .eq(ChatMessage::getSessionId, session.getId())
                            .eq(ChatMessage::getMsgType, "text")
                            .ne(ChatMessage::getSenderId, senderId)
                            .eq(ChatMessage::getReplied, 0)
                            .orderByDesc(ChatMessage::getId)
                            .last("LIMIT 1"));

            if (lastMsg != null) {
                lastMsg.setReplied(1);
                messageMapper.updateById(lastMsg);

                pinkEarned = pinkDiamondReward;
                int currentPink = senderBalance.getPinkDiamonds() != null ? senderBalance.getPinkDiamonds() : 0;
                senderBalance.setPinkDiamonds(currentPink + pinkDiamondReward);

                DiamondRecord pr = new DiamondRecord();
                pr.setUserId(senderId);
                pr.setType("in");
                pr.setAmount(pinkDiamondReward);
                pr.setBalanceAfter(senderBalance.getPinkDiamonds());
                pr.setBizType("chat_reply");
                pr.setRemark("回复奖励·星尘");
                diamondRecordMapper.insert(pr);
            }

            balanceMapper.updateById(senderBalance);
        }

        ChatMessage msg = new ChatMessage();
        msg.setSessionId(session.getId());
        msg.setSenderId(senderId);
        msg.setSenderRole(senderRole);
        msg.setMsgType("text");
        msg.setContent(request.getContent());
        msg.setCostDiamond(costDiamond);
        msg.setMsgTime(now);
        msg.setIsRead(0);
        msg.setReplied(0);
        messageMapper.insert(msg);

        session.setLastMsg(request.getContent());
        session.setLastTime(now);
        if (isMale) {
            session.setFemaleUnread((session.getFemaleUnread() == null ? 0 : session.getFemaleUnread()) + 1);
        } else {
            session.setMaleUnread((session.getMaleUnread() == null ? 0 : session.getMaleUnread()) + 1);
        }
        sessionMapper.updateById(session);

        Map<String, Object> pushData = new HashMap<>();
        pushData.put("type", "new_message");
        pushData.put("sessionId", session.getId());
        pushData.put("content", msg.getContent());
        pushData.put("senderId", senderId);
        ChatWebSocketHandler.sendToUser(receiverId, JSONUtil.toJsonStr(pushData));

        return ChatMessageVO.builder()
                .id(msg.getId())
                .sessionId(msg.getSessionId())
                .senderId(msg.getSenderId())
                .senderRole(msg.getSenderRole())
                .msgType(msg.getMsgType())
                .content(msg.getContent())
                .costDiamond(msg.getCostDiamond())
                .msgTime(msg.getMsgTime())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatSessionVO getOrCreateSession(Long currentUserId, Long peerUserId, boolean isFake) {
        ChatSession session;
        Long maleId, femaleId;

        if (isFake) {
            // 假用户场景：当前用户固定为 male，假用户固定为 female，需用 is_fake 区分
            maleId = currentUserId;
            femaleId = peerUserId;
            session = sessionMapper.selectOne(
                    new LambdaQueryWrapper<ChatSession>()
                            .eq(ChatSession::getMaleUserId, maleId)
                            .eq(ChatSession::getFemaleUserId, femaleId)
                            .eq(ChatSession::getIsFake, true));
        } else {
            // 真实用户场景：小ID放male，大ID放female，is_fake=false
            maleId = currentUserId < peerUserId ? currentUserId : peerUserId;
            femaleId = currentUserId < peerUserId ? peerUserId : currentUserId;
            session = sessionMapper.selectOne(
                    new LambdaQueryWrapper<ChatSession>()
                            .eq(ChatSession::getMaleUserId, maleId)
                            .eq(ChatSession::getFemaleUserId, femaleId)
                            .eq(ChatSession::getIsFake, false));
        }

        if (session == null) {
            session = new ChatSession();
            session.setMaleUserId(maleId);
            session.setFemaleUserId(femaleId);
            session.setIsFake(isFake);
            session.setType("chat");
            session.setMaleUnread(0);
            session.setFemaleUnread(0);
            sessionMapper.insert(session);
        }

        return ChatSessionVO.builder()
                .sessionId(session.getId())
                .peerUserId(peerUserId)
                .isFake(isFake)
                .build();
    }
}
