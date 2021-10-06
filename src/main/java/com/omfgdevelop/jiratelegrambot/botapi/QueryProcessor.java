package com.omfgdevelop.jiratelegrambot.botapi;

import com.omfgdevelop.jiratelegrambot.botapi.handlers.CallBackQueryHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class QueryProcessor {

    private final Map<UserState, CallBackQueryHandler> queryHandlerMap = new HashMap<>();

    public QueryProcessor(List<CallBackQueryHandler> queryHandlers) {
        queryHandlers.forEach(handler -> this.queryHandlerMap.put(handler.getHandlerName(), handler));
    }

    public SendMessage processQuery(UserState state, Update update) {
        CallBackQueryHandler handler = queryHandlerMap.get(state);
        return handler.handleCallbackQuery(update.getCallbackQuery());
    }

}
