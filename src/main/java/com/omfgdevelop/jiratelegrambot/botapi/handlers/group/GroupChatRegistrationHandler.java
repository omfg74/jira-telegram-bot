package com.omfgdevelop.jiratelegrambot.botapi.handlers.group;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.service.GroupChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

import static com.omfgdevelop.jiratelegrambot.botapi.UserState.REGISTER_CHAT;

@Component
@RequiredArgsConstructor
public class GroupChatRegistrationHandler implements MessageHandler {

    private final GroupChatService groupChatService;

    @Override
    public SendMessage handleInputMessage(Message message) {
        return groupChatService.registerNewChat(message.getChat());
    }

    @Override
    public UserState getHandlerName() {
        return REGISTER_CHAT;
    }
}
