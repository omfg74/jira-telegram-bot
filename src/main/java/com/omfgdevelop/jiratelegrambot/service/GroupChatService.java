package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.entity.GroupChat;
import com.omfgdevelop.jiratelegrambot.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatService {

    private final GroupRepository groupRepository;

    public List<GroupChat> getRegisteredChats() {
        return groupRepository.findAllRegistered().orElse(new ArrayList<>());
    }

    public List<GroupChat> getAll() {
        return groupRepository.findAll();
    }

    public GroupChat getRegisteredChatByTelegramId(Long chatId) {
        return groupRepository.findRegisteredChatByTelegramId(chatId).orElse(null);
    }
    public GroupChat getRegisteredChatById(Long chatId) {
        return groupRepository.findRegisteredChatById(chatId).orElse(null);
    }

    public SendMessage registerNewChat(Chat chat) {
        if (!groupRepository.findByChatId(chat.getId()).isPresent()) {
            GroupChat groupChat = GroupChat.builder()
                    .chatName(chat.getTitle())
                    .telegramId(chat.getId())
                    .active(false)
                    .deleted(false)
                    .build();
            groupRepository.save(groupChat);
            return new SendMessage(String.valueOf(chat.getId()), HandlerConstants.CHAT_HAS_BEEN_REGISTERED);
        } else {
            return new SendMessage(String.valueOf(chat.getId()), HandlerConstants.CURRENT_CHAT_ALREADY_REGISTERED);
        }
    }

    @Transactional
    public void updateActiveAndProjectLink(Long chatId, Boolean active, String projectId) {
        groupRepository.updateActiveAndProjectLink(chatId,active,projectId);
    }
}
