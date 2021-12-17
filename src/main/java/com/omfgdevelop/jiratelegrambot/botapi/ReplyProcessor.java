package com.omfgdevelop.jiratelegrambot.botapi;

import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.omfgdevelop.jiratelegrambot.botapi.UserState.STAND_BY;

@Service
@Log4j2
public class ReplyProcessor {

    private final Map<UserState, MessageHandler> messageHandlers = new EnumMap<>(UserState.class);

    private final UserStateCache userStateCache;


    public ReplyProcessor(List<MessageHandler> messageHandlers, UserStateCache userStateCache) {
        this.userStateCache = userStateCache;
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processMessage(UserState userState, Message message) {
        try {
            MessageHandler currentHandler = messageHandlers.get(userState);
            return currentHandler.handleInputMessage(message);
        } catch (Exception e) {
            if (!userState.equals(UserState.NEW_JIRA_PASSWORD)) {
                log.error(new EcsEvent("Error").with(e).withContext("id", message.getFrom().getId()).withContext("text", message.getText()));
            }
            userStateCache.setCurrentUserState(message.getFrom().getId(), STAND_BY);
            return new SendMessage(message.getChatId(), "Unsupported command");
        }
    }
}
