package com.omfgdevelop.jiratelegrambot.botapi.handlers.workflow;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

import static com.omfgdevelop.jiratelegrambot.botapi.UserState.PROJECT_SELECT;

@Component
@RequiredArgsConstructor
public class ProjectSelectHandler implements MessageHandler {
    @Override
    public SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        return new SendMessage(String.valueOf(message.getChatId()), "Сначала выберите проект для этой задачи");
    }

    @Override
    public UserState getHandlerName() {
        return PROJECT_SELECT;
    }
}
