package com.omfgdevelop.jiratelegrambot.botapi.handlers.task;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.enums.TaskStatus;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.omfgdevelop.jiratelegrambot.Common.createProjectMenu;
import static com.omfgdevelop.jiratelegrambot.LogConstants.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class NewTaskCreationHandler implements MessageHandler {

    private final UserService userService;

    private final UserStateCache userStateCache;

    private final TaskService taskService;

    private final JiraProjectService projectService;


    @Override
    public SendMessage handleInputMessage(Message message) {
        Task task = taskService.checkIfTaskIsStarted(message);
        if (task == null) {
            User user = userService.getUserByTelegramId((long) message.getFrom().getId());
            if (Boolean.TRUE.equals(!user.getActive()) && Boolean.TRUE.equals(user.getDeleted()))
                return new SendMessage(message.getChatId().toString(), HandlerConstants.USER_BLOCKED_OR_DELETED);

            Task createdTask = Task.builder()
                    .telegramId((long) message.getFrom().getId())
                    .status(TaskStatus.TITLE_INPUT.getValue())
                    .build();
            taskService.insertTask(createdTask);
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TITLE);
            if (user != null) {
                return new SendMessage(String.valueOf(message.getChatId()), String.format(HandlerConstants.NEW_TASK_AND_AUTHOR, user.getJiraUsername()));
            } else {
                userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.UNREGISTERED);
                userStateCache.removeIdFromCache(message.getFrom().getId());
                return new SendMessage(String.valueOf(message.getChatId()), String.format(HandlerConstants.NO_SUCH_USER_REGISTER, message.getFrom().getId()));
            }
        } else {
            switch (task.getStatus()) {
                case 4:
                    userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TITLE);
                    return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.YOU_HAVE_PENDING_TASK_ENTER_NAME);
                case 5:
                    userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TEXT);
                    return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.YOU_HAVE_PENDING_TASK_ENTER_TEXT);
                case 6:
                    userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.PROJECT_SELECT);
                    return createProjectMenu(message, task, projectService);
            }
        }
        log.error(new EcsEvent(UNEXPECTED_ERROR_CASE).withContext(CASE, task.getStatus()));
        return new SendMessage(String.valueOf(message.getChatId()), UNEXPECTED_ERROR);

    }


    @Override
    public UserState getHandlerName() {
        return UserState.CREATING_NEW_TASK;
    }
}
