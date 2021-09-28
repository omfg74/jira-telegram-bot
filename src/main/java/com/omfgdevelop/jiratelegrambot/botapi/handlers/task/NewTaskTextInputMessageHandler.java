package com.omfgdevelop.jiratelegrambot.botapi.handlers.task;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class NewTaskTextInputMessageHandler implements MessageHandler {

    private final TaskService taskService;

    private final UserStateCache userStateCache;

    @Override
    public SendMessage handleInputMessage(Message message) {
        Task task = taskService.inputTaskText(message);
        userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.STAND_BY);
        if (task != null) {
            return new SendMessage(message.getChatId(), String.format("To task with title %s added text %s", task.getTaskTitle(), task.getTaskText()));
        }
        return new SendMessage(message.getChatId(), "Something wrong you have no pending tasks");
    }

    @Override
    public UserState getHandlerName() {
        return UserState.NEW_TASK_TEXT;
    }
}
