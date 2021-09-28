package com.omfgdevelop.jiratelegrambot.botapi;

import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ReplyProcessor {

    private final Map<UserState, MessageHandler> messageHandlers = new HashMap<>();

    public ReplyProcessor(List<MessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processMessage(UserState userState, Message message) {
        try {
            MessageHandler currentHandler = messageHandlers.get(userState);
            return currentHandler.handleInputMessage(message);
        }catch (Exception e){
            return new SendMessage(message.getChatId(),"Unsupported command");
        }
    }
}
