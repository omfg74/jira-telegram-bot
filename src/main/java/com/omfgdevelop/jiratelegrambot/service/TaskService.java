package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.enums.TaskStatus;
import com.omfgdevelop.jiratelegrambot.repository.TaskQueueRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

import static com.omfgdevelop.jiratelegrambot.enums.TaskStatus.CREATED;
import static com.omfgdevelop.jiratelegrambot.enums.TaskStatus.DONE;

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
        task.setStatus(TaskStatus.TITLE_INPUT.getValue());
        task.setTelegramId((long) message.getFrom().getId());
        task.setTaskTitle(message.getText());
        userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.PROJECT_SELECT);
        return taskQueueRepository.save(task);
    }

    public Task insertTask(Task task) {
        return taskQueueRepository.save(task);
    }

    public Task inputTaskText(Message message) {
        Optional<Task> taskOptional = taskQueueRepository.findByTelegramIdAndStatus((long) message.getFrom().getId(), TaskStatus.TEXT_INPUT.getValue());
        if (taskOptional.isPresent()) {
            taskOptional.get().setTaskText(message.getText());
            taskOptional.get().setStatus(CREATED.getValue());
            return taskQueueRepository.save(taskOptional.get());
        } else {
            return null;
        }
    }

    public Task getTaskByTelegramId(Long telegramId) throws NotFoundException {
        Optional<Task> taskOptional = taskQueueRepository.findByTelegramIdAndStatus(telegramId, CREATED.getValue());
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new NotFoundException("Task not found");
    }

    public Task getCreatedTaskByTelegramIdAndTitle(Long telegramId, String title) throws NotFoundException {
        Optional<Task> taskOptional = taskQueueRepository.findByTelegramIdAndStatusAndTitleLast(telegramId, CREATED.getValue(), title);
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new NotFoundException("Task not found");
    }

    public Task getTaskByTelegramIdAndTitle(Long telegramId, String title, TaskStatus status) throws NotFoundException {
        Optional<Task> taskOptional = taskQueueRepository.findByTelegramIdAndStatusLast(telegramId, status.getValue());
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new NotFoundException("Task not found");
    }

    public Task getTaskByTelegramIdAndTitleAndState(Long telegramId, String title, TaskStatus status) throws NotFoundException {
        Optional<Task> taskOptional = taskQueueRepository.findTaskByTelegramIdAndTaskTitleAndStatus(telegramId, title, status.getValue());
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new NotFoundException("Task not found");
    }

    public void markTaskAsDone(Task task) {
        Optional<Task> taskOptional = taskQueueRepository.findById(task.getId());
        if (taskOptional.isPresent()) {
            Task taskToUpdate = taskOptional.get();
            taskToUpdate.setStatus(DONE.getValue());
            taskQueueRepository.save(taskToUpdate);
        }
    }

    public void updateTask(Task task) {
        Optional<Task> taskOptional = taskQueueRepository.findById(task.getId());
        if (taskOptional.isPresent()) {
            task.setStatus(task.getStatus() != null ? task.getStatus() : taskOptional.get().getStatus());
            task.setTelegramId(task.getTelegramId() != null ? task.getTelegramId() : taskOptional.get().getTelegramId());
            task.setTaskTitle(task.getTaskTitle() != null ? task.getTaskTitle() : taskOptional.get().getTaskTitle());
            task.setProject(task.getProject() != null ? task.getProject() : taskOptional.get().getProject());
            task.setTaskText(task.getTaskText() != null ? task.getTaskText() : taskOptional.get().getTaskText());
            taskQueueRepository.save(task);
        }
    }

    public List<Task> getAllCreatedTasks() {
        return taskQueueRepository.findAllCreatedTasks();
    }

    public void markTaskAsError(Task task) {
        task.setStatus(TaskStatus.ERROR.getValue());
        taskQueueRepository.save(task);
    }

    public void markTaskAsProcessed(Task task) {
        task.setStatus(TaskStatus.PROCESSING.getValue());
        taskQueueRepository.save(task);
    }
}
