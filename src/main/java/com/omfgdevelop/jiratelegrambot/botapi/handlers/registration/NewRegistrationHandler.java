package com.omfgdevelop.jiratelegrambot.botapi.handlers.registration;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class NewRegistrationHandler implements MessageHandler {

    private final UserStateCache userStateCache;

    @Override
    public SendMessage handleInputMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Пользователь не зарегистрирован.\nВведите имя пользователя от JIRA");
        sendMessage.setChatId(message.getChatId());
        userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_USER_NAME);
        return sendMessage;
    }

    @Override
    public UserState getHandlerName() {
        return UserState.UNREGISTERED;
    }
}
