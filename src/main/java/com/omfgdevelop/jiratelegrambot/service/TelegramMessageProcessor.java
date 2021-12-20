package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.botapi.QueryProcessor;
import com.omfgdevelop.jiratelegrambot.botapi.ReplyProcessor;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

import static com.omfgdevelop.jiratelegrambot.CommandConstants.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class TelegramMessageProcessor {

    private final UserStateCache userStateCache;

    private final ReplyProcessor replyProcessor;

    private final QueryProcessor queryProcessor;

    @Value("${app.bot.address}")
    private String botAddress;

    @Value("${app.bot.group.enable}")
    private boolean groupChatEnable;

    public BotApiMethod<? extends Serializable> handleMessage(Update update) {
        SendMessage replyMessage = null;

        if (fromGroup(update.getMessage())) {
            if (!update.getMessage().getText().contains(botAddress) || !groupChatEnable) return null;
            return handleGroupMessage(update.getMessage());
        }

        if (update.hasCallbackQuery()) {
            Long userId = (long) update.getCallbackQuery().getFrom().getId();
            switch (userStateCache.getCurrentUserState(userId)) {
                case PROJECT_SELECT:
                    return queryProcessor.processQuery(userStateCache.getCurrentUserState(userId), update);
                default:
                    userStateCache.setCurrentUserState(userId, userStateCache.getCurrentUserState(userId));
            }
            log.debug("New query from user: {} with data: {}", update.getCallbackQuery().getFrom().getUserName(), update.getCallbackQuery().getData());
            return new SendMessage(String.valueOf(update.getCallbackQuery().getMessage().getChatId()), "Задача уже назначена на проект");
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.debug("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private BotApiMethod<?> handleGroupMessage(Message message) {
        UserState userState;
        if (message.getText() != null) {
            switch (message.getText()) {
                case CREATE_NEW_TASK:
                case START:
                case CANCEL_CURRENT_TASK:
                case DELETE_USER:
                    return new SendMessage(String.valueOf(message.getChatId()), String.format(HandlerConstants.GO_TO_MAIN_CHAT, botAddress));
                case "/register_chat":
                    userState = UserState.REGISTER_CHAT;
                    break;
                default:
                    userState = UserState.GROUP_CHAT_MESSAGE;
                    break;
            }
            userStateCache.setCurrentUserState(message.getChatId(), userState);
            return replyProcessor.processGroupMessage(message);

        }
        return null;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        SendMessage replyMessage;
        long userId = message.getFrom().getId();
        UserState userState;
        if (userStateCache.isRegistered(userId)) {
            switch (inputMessage) {
                case CREATE_NEW_TASK:
                case START:
                    userState = UserState.CREATING_NEW_TASK;
                    break;
                case CANCEL_CURRENT_TASK:
                    userState = UserState.CANCELLING_CURRENT_TASK;
                    break;
                case DELETE_USER:
                    userState = UserState.DELETE_REQUEST;
                    break;
                default:
                    userState = userStateCache.getCurrentUserState(userId);
            }
        } else if (inputMessage.equalsIgnoreCase("/delete_user")) {
            userState = UserState.DELETE_REQUEST;
        } else {
            userState = userStateCache.getCurrentUserState(userId);
        }
        userStateCache.setCurrentUserState(userId, userState);
        replyMessage = replyProcessor.processMessage(userStateCache.getCurrentUserState(userId), message);

        return replyMessage;
    }

    private boolean fromGroup(Message message) {
        if (message == null) return false;
        return message.getChat().getType().equals("group");
    }

    boolean isWaitingForQuery(UserState userState) {
        switch (userState) {
            case PROJECT_SELECT:
                return true;
            default:
                return false;
        }
    }
}
