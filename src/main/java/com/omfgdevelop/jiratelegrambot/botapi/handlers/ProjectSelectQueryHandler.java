package com.omfgdevelop.jiratelegrambot.botapi.handlers;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.enums.TaskStatus;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static com.omfgdevelop.jiratelegrambot.enums.TaskStatus.PROJECT_INPUT;
import static com.omfgdevelop.jiratelegrambot.enums.TaskStatus.TEXT_INPUT;


@Component
@RequiredArgsConstructor
@Log4j2
public class ProjectSelectQueryHandler implements CallBackQueryHandler {

    private final TaskService taskService;

    private final UserStateCache userStateCache;

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        Task task = null;
        try {
            task = taskService.getTaskByTelegramIdAndTitleAndState((long) callbackQuery.getFrom().getId(), callbackQuery.getData().split("/")[0], PROJECT_INPUT);
            if (task != null && task.getTaskText() == null) {
                task.setProject(callbackQuery.getData().split("/")[1]);
                task.setStatus(TEXT_INPUT.getValue());
                taskService.insertTask(task);
            }
        } catch (NotFoundException e) {
            log.error(new EcsEvent("Error adding project to task")
                    .withContext("telegram_id", callbackQuery.getFrom().getId()));
            return new SendMessage(callbackQuery.getMessage().getChatId(),"Error finding current task");
        }
        userStateCache.setCurrentUserState(callbackQuery.getFrom().getId(), UserState.NEW_TASK_TEXT);
        return new SendMessage(callbackQuery.getMessage().getChatId(), String.format("Задача %s добавлена к проекту %s.\nДобавьте описание задачи",
                task != null ? task.getTaskTitle() : null, task != null ? task.getProject() : null));
    }

    @Override
    public UserState getHandlerName() {
        return UserState.PROJECT_SELECT;
    }
}
