package com.omfgdevelop.jiratelegrambot.botapi.handlers.registration;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

@Component
@RequiredArgsConstructor
public class UserNameInputMessageHandler implements MessageHandler {

    private final UserService userService;

    private final UserStateCache userStateCache;

    @Override
    public SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        User exists = userService.getUserByUserId((long)message.getFrom().getId());
        SendMessage sendMessage = new SendMessage();
        if (exists == null) {
            User user = new User();
            user.setTelegramId((long) message.getFrom().getId());
            user.setJiraUsername(message.getText());
            user.setTelegramUsername(message.getFrom().getFirstName());
            userService.createOrUpdate(user);
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(String.format("User %s username added. Enter Jira password. It will be stored safely", message.getText()));
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_JIRA_PASSWORD);
        } else {
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(String.format("Пользоветель с именем %s уже есть", message.getText()));
        }
        return sendMessage;
    }

    @Override
    public UserState getHandlerName() {
        return UserState.NEW_USER_NAME;
    }
}
