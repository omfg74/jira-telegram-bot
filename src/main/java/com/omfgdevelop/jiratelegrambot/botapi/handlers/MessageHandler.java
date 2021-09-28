package com.omfgdevelop.jiratelegrambot.botapi.handlers;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {


    SendMessage handleInputMessage(Message message);

    UserState getHandlerName();
}
