package com.omfgdevelop.jiratelegrambot.botapi.handlers.workflow;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StandByHandler implements MessageHandler {
    @Override
    public SendMessage handleInputMessage(Message message) {
        return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.START_MAESSGE);
    }

    @Override
    public UserState getHandlerName() {
        return UserState.STAND_BY;
    }
}
