package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.botapi.ReplyProcessor;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j2
@RequiredArgsConstructor
public class TelegramMessageProcessor {

    private final UserService userService;

    private final UserStateCache userStateCache;

    private final ReplyProcessor replyProcessor;

    public SendMessage handleMessage(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            log.info("New query from user: {} with data: {}", update.getCallbackQuery().getFrom().getUserName(), update.getCallbackQuery().getData());
            return new SendMessage(update.getCallbackQuery().getFrom().getUserName(), "CallBAckquerry not supporting");
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
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
                default:
                    userState = userStateCache.getCurrentUserState(userId);
            }
        } else {
            userState = userStateCache.getCurrentUserState(userId);
        }
        userStateCache.setCurrentUserState(userId, userState);
        replyMessage = replyProcessor.processMessage(userStateCache.getCurrentUserState(userId), message);

        return replyMessage;
    }

}
