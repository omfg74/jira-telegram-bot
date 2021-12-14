package com.omfgdevelop.jiratelegrambot.botapi.handlers.user;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

import static com.omfgdevelop.jiratelegrambot.botapi.UserState.STAND_BY;
import static com.omfgdevelop.jiratelegrambot.botapi.UserState.USER_DELETE_CONFIRM;

@Component
@RequiredArgsConstructor
public class UserDeleteHandler implements MessageHandler {

    private final UserService userService;

    private final UserStateCache userStateCache;

    @Override
    public SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        if (message.getText().equals("YES")) {
            userService.deleteUser(message.getChatId());
            userStateCache.removeIdFromCache(message.getChatId());
            return new SendMessage(message.getChatId(), "Пользователь удален. Можно зарегистрировать нового.\nВведите /start для повторной регистрации.");
        } else {
            userStateCache.setCurrentUserState(message.getFrom().getId(), STAND_BY);
            return new SendMessage(message.getChatId(), "Действие отменено.");
        }
    }

    @Override
    public UserState getHandlerName() {
        return USER_DELETE_CONFIRM;
    }
}
