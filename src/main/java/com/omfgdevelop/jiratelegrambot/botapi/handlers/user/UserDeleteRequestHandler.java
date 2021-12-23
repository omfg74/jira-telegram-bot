package com.omfgdevelop.jiratelegrambot.botapi.handlers.user;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

import static com.omfgdevelop.jiratelegrambot.botapi.UserState.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserDeleteRequestHandler implements MessageHandler {

    private final UserService userService;

    private final UserStateCache userStateCache;


    @Override
    public SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        User user = userService.getUserByTelegramId(message.getChatId());
        if (user != null) {
            userStateCache.setCurrentUserState(message.getFrom().getId(), USER_DELETE_CONFIRM);
            return new SendMessage(String.valueOf(message.getChatId()), String.format(HandlerConstants.ARE_YOU_SURE_DELETE_USER, user.getJiraUsername()));
        } else {
            userStateCache.setCurrentUserState(message.getFrom().getId(), STAND_BY);
            log.error(new EcsEvent(HandlerConstants.USER_NOT_FOUND).withContext("telegram_id", message.getChat()));
            return new SendMessage(String.valueOf(message.getChatId()), String.format(HandlerConstants.USER_NOT_FOUND, message.getChatId()));
        }

    }

    @Override
    public UserState getHandlerName() {
        return DELETE_REQUEST;
    }
}
