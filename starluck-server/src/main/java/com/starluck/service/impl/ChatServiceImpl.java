package com.starluck.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starluck.common.BusinessException;
import com.starluck.config.ChatWebSocketHandler;
import com.starluck.dto.ChatSendRequest;
import com.starluck.entity.*;
import com.starluck.mapper.*;
import com.starluck.service.ChatService;
import com.starluck.vo.ChatMessageVO;
import com.starluck.vo.ChatSessionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 聊天服务实现
 *
 * @author AI
 * @date 2026-06-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;
    private final UserBalanceMapper balanceMapper;
    private final DiamondRecordMapper diamondRecordMapper;
    private final FakeUserMapper fakeUserMapper;
    private final UserProfileMapper userProfileMapper;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

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
            int unread = isMale ? s.getMaleUnread() : s.getFemaleUnread();

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

        // 清除该用户的未读数
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

        // 判断发送者是男用户还是女用户
        boolean isMale = session.getMaleUserId().equals(senderId);
        Long receiverId = isMale ? session.getFemaleUserId() : session.getMaleUserId();
        String senderRole = isMale ? "male" : "female";

        String now = LocalDateTime.now().format(TIME_FMT);
        int costDiamond = 0;

        // 男用户发送文字消息扣费
        if (isMale && !session.getIsFake()) {
            UserBalance balance = balanceMapper.selectOne(
                    new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, senderId));
            if (balance == null || balance.getDiamonds() < 1) {
                throw new BusinessException("钻石余额不足，请充值");
            }

            // VIP用户每日有免费额度
            if (balance.getIsVip() == 1 && balance.getDailyFreeChat() > 0) {
                balance.setDailyFreeChat(balance.getDailyFreeChat() - 1);
            } else {
                costDiamond = 1;
                balance.setDiamonds(balance.getDiamonds() - 1);

                // 记录钻石流水
                DiamondRecord dr = new DiamondRecord();
                dr.setUserId(senderId);
                dr.setType("out");
                dr.setAmount(-1);
                dr.setBalanceAfter(balance.getDiamonds());
                dr.setBizType("chat");
                dr.setRemark("聊天扣费");
                diamondRecordMapper.insert(dr);
            }
            balanceMapper.updateById(balance);
        }

        // 保存消息
        ChatMessage msg = new ChatMessage();
        msg.setSessionId(session.getId());
        msg.setSenderId(senderId);
        msg.setSenderRole(senderRole);
        msg.setMsgType("text");
        msg.setContent(request.getContent());
        msg.setCostDiamond(costDiamond);
        msg.setMsgTime(now);
        messageMapper.insert(msg);

        // 更新会话
        session.setLastMsg(request.getContent());
        session.setLastTime(now);
        if (isMale) {
            session.setFemaleUnread(session.getFemaleUnread() + 1);
        } else {
            session.setMaleUnread(session.getMaleUnread() + 1);
        }
        sessionMapper.updateById(session);

        // 通过WebSocket推送给对方
        Map<String, Object> pushData = new HashMap<>();
        pushData.put("type", "new_message");
        pushData.put("sessionId", session.getId());
        pushData.put("message", ChatMessageVO.builder()
                .id(msg.getId())
                .sessionId(msg.getSessionId())
                .senderId(msg.getSenderId())
                .senderRole(msg.getSenderRole())
                .msgType(msg.getMsgType())
                .content(msg.getContent())
                .costDiamond(msg.getCostDiamond())
                .msgTime(msg.getMsgTime())
                .build());
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
    public ChatSessionVO getOrCreateSession(Long maleUserId, Long femaleUserId, boolean isFake) {
        ChatSession session = sessionMapper.selectOne(
                new LambdaQueryWrapper<ChatSession>()
                        .eq(ChatSession::getMaleUserId, maleUserId)
                        .eq(ChatSession::getFemaleUserId, femaleUserId));

        if (session == null) {
            session = new ChatSession();
            session.setMaleUserId(maleUserId);
            session.setFemaleUserId(femaleUserId);
            session.setIsFake(isFake);
            session.setType("chat");
            sessionMapper.insert(session);
        }

        return ChatSessionVO.builder()
                .sessionId(session.getId())
                .peerUserId(femaleUserId)
                .isFake(isFake)
                .build();
    }
}
