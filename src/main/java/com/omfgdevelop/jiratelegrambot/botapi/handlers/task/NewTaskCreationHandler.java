package com.omfgdevelop.jiratelegrambot.botapi.handlers.task;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class NewTaskCreationHandler implements MessageHandler {

    private final UserService userService;

    private final UserStateCache userStateCache;

    private final TaskService taskService;

    @Override
    public SendMessage handleInputMessage(Message message) {
        Task task = taskService.checkIfTaskIsStarted(message);
        if (task == null) {
            User user = userService.getUserByUserId(message.getFrom().getId());
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TITLE);
            if (user != null) {
                return new SendMessage(message.getChatId(), String.format("Ready to create task for user %s.\nEnter task title", user.getJiraUsername()));
            } else {
                return new SendMessage(message.getChatId(), String.format("no user found by userId %s", message.getFrom().getId()));
            }
        } else {
            switch (task.getStatus()) {
                case 4:
                    userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TITLE);
                    return new SendMessage(message.getChatId(), "You have started task in status input title");
                case 5:
                    userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TEXT);
                    return new SendMessage(message.getChatId(), "You have started task in status input text");
            }
        }
        return new SendMessage(message.getChatId(), "Unexpected error");

    }

    @Override
    public UserState getHandlerName() {
        return UserState.CREATING_NEW_TASK;
    }
}
