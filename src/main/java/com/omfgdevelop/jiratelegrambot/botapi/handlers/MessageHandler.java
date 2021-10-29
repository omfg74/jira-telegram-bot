package com.omfgdevelop.jiratelegrambot.botapi.handlers;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

public interface MessageHandler {


    SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException;

    UserState getHandlerName();
}
