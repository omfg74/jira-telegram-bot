package com.omfgdevelop.jiratelegrambot.botapi.handlers.cancel;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

@Component
@RequiredArgsConstructor
public class CancelTaskHandler implements MessageHandler {

    private final UserStateCache userStateCache;

    private final TaskService taskService;

    @Override
    public SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {

        taskService.dropAllPendingTasks(message.getFrom().getId());
        userStateCache.setCurrentUserState(message.getFrom().getId(),UserState.STAND_BY);
        return new SendMessage(String.valueOf(message.getChatId()),"Все незаконченные задачи отменены. Можно создать новую");
    }

    @Override
    public UserState getHandlerName() {
        return UserState.CANCELLING_CURRENT_TASK;
    }
}
