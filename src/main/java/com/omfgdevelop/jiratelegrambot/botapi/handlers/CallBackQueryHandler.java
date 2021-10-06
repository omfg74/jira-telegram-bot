package com.omfgdevelop.jiratelegrambot.botapi.handlers;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallBackQueryHandler {

    SendMessage handleCallbackQuery(CallbackQuery update);

    UserState getHandlerName();

}
