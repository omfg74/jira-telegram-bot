package com.omfgdevelop.jiratelegrambot.botapi.handlers.task;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class TaskTitleInputHandler implements MessageHandler {

    private final TaskService taskService;

    @Override
    public SendMessage handleInputMessage(Message message) {
        Task task = taskService.getOrCreateNewTask(message);
        return new SendMessage(message.getChatId(), String.format("Task created with title %s.\n Enter task text", task.getTaskTitle()));
    }

    @Override
    public UserState getHandlerName() {
        return UserState.NEW_TASK_TITLE;
    }
}
