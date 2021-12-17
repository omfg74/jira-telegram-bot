package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.botapi.QueryProcessor;
import com.omfgdevelop.jiratelegrambot.botapi.ReplyProcessor;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.omfgdevelop.jiratelegrambot.botapi.UserState.STAND_BY;

@Service
@Log4j2
@RequiredArgsConstructor
public class TelegramMessageProcessor {

    private final UserService userService;

    private final UserStateCache userStateCache;

    private final ReplyProcessor replyProcessor;

    private final QueryProcessor queryProcessor;

    public SendMessage handleMessage(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            Long userId = (long) update.getCallbackQuery().getFrom().getId();
            switch (userStateCache.getCurrentUserState(userId)) {
                case PROJECT_SELECT:
                    return queryProcessor.processQuery(userStateCache.getCurrentUserState(userId), update);
                default:
                    userStateCache.setCurrentUserState(userId, STAND_BY);
            }
            log.debug("New query from user: {} with data: {}", update.getCallbackQuery().getFrom().getUserName(), update.getCallbackQuery().getData());
            return new SendMessage(update.getCallbackQuery().getMessage().getChatId(), "CallBackquerry not supporting");
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.debug("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        SendMessage replyMessage;
        long userId = message.getFrom().getId();
        UserState userState;
        if (userStateCache.isRegistered(userId)) {
            switch (inputMessage) {
                case "/create_new_task":
                case "/start":
                    userState = UserState.CREATING_NEW_TASK;
                    break;
                case "/cancel_current_task":
                    userState = UserState.CANCELLING_CURRENT_TASK;
                    break;
                case "/delete_user":
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

    boolean isWaitingForQuery(UserState userState) {
        switch (userState) {
            case PROJECT_SELECT:
                return true;
            default:
                return false;
        }
    }
}
