package com.omfgdevelop.jiratelegrambot.botapi.handlers.task;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.enums.TaskStatus;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.omfgdevelop.jiratelegrambot.Common.createProjectMenu;

@Component
@RequiredArgsConstructor
@Log4j2
public class TaskTitleInputHandler implements MessageHandler {

    private final TaskService taskService;

    private final JiraProjectService projectService;

    private final UserStateCache userStateCache;

    @Override
    public SendMessage handleInputMessage(Message message) {
        Task task = null;
        try {
            task = taskService.getTaskByTelegramIdAndTitle((long) message.getFrom().getId(), null, TaskStatus.TITLE_INPUT);
            task.setTaskTitle(message.getText());
            task.setStatus(TaskStatus.PROJECT_INPUT.getValue());
            taskService.updateTask(task);
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.PROJECT_SELECT);
            return createProjectMenu(message, task,projectService);
        } catch (NotFoundException e) {
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.STAND_BY);
            log.error(new EcsEvent("Error title input"));
            return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.NO_TASK_FOUND);
        }
    }

    @Override
    public UserState getHandlerName() {
        return UserState.NEW_TASK_TITLE;
    }

}
