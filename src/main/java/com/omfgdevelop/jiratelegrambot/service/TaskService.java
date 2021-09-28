package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.enums.TaskStatus;
import com.omfgdevelop.jiratelegrambot.repository.TaskQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskQueueRepository taskQueueRepository;

    private final UserStateCache userStateCache;

    public Task checkIfTaskIsStarted(Message message) {
        Optional<Task> taskQueueOptional = taskQueueRepository.findByTelegramIdAndStatusNewTitleOrNewText((long) message.getFrom().getId());
        if (taskQueueOptional.isPresent()) {
            return taskQueueOptional.get();
        }
        return null;
    }

    public Task getOrCreateNewTask(Message message) {
        Task task;
        Optional<Task> taskQueueOptional = taskQueueRepository.findByTelegramIdAndStatusNewTitleOrNewText((long) message.getFrom().getId());
        task = taskQueueOptional.orElseGet(Task::new);
        task.setStatus(TaskStatus.TEXT_INPUT.getValue());
        task.setTelegramId((long) message.getFrom().getId());
        task.setTaskTitle(message.getText());
        userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TEXT);
        return taskQueueRepository.save(task);
    }

    public Task inputTaskText(Message message) {
        Optional<Task> taskOptional = taskQueueRepository.findByTelegramIdAndStatus((long) message.getFrom().getId(), TaskStatus.TEXT_INPUT.getValue());
        if (taskOptional.isPresent()) {
            taskOptional.get().setTaskText(message.getText());
            taskOptional.get().setStatus(TaskStatus.CREATED.getValue());
            return taskQueueRepository.save(taskOptional.get());
        } else {
            return null;
        }
    }
}
