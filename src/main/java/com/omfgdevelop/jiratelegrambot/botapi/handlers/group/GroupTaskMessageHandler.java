package com.omfgdevelop.jiratelegrambot.botapi.handlers.group;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.GroupChat;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.enums.TaskStatus;
import com.omfgdevelop.jiratelegrambot.enums.TaskType;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.GroupChatService;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

import static com.omfgdevelop.jiratelegrambot.LogConstants.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class GroupTaskMessageHandler implements MessageHandler {

    private final TaskService taskService;

    private final UserService userService;

    private final GroupChatService groupChatService;

    @Value("${app.bot.address}")
    private String botName;

    @Override
    public SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        User user = userService.getUserByTelegramId(message.getFrom().getId());
        GroupChat groupChat = groupChatService.getRegisteredChatById(message.getChatId());
        if (groupChat == null) {
            log.error(new EcsEvent(HandlerConstants.NOT_PERMITTED_GROUP_CHAT).withContext(CHAT_ID, message.getChat().getId()).withContext(USER_ID, message.getFrom().getId()).withContext(MESSAGE_TEXT, message.getText()).withContext(USER_NAME, message.getFrom().getUserName()));
            return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.NOT_PERMITTED_GROUP_CHAT);
        }
        if (Boolean.FALSE.equals(groupChat.getActive())) {
            log.error(new EcsEvent(HandlerConstants.NOT_PERMITTED_GROUP_CHAT).withContext(CHAT_ID, message.getChat().getId()).withContext(USER_ID, message.getFrom().getId()).withContext(MESSAGE_TEXT, message.getText()).withContext(USER_NAME, message.getFrom().getUserName()));
            return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.NOT_PERMITTED_GROUP_CHAT);
        }
        if (Boolean.TRUE.equals(groupChat.getDeleted())) {
            log.error(new EcsEvent(HandlerConstants.DELETED_GROUP_CHAT).withContext(CHAT_ID, message.getChat().getId()).withContext(USER_ID, message.getFrom().getId()).withContext(MESSAGE_TEXT, message.getText()).withContext(USER_NAME, message.getFrom().getUserName()));
            return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.DELETED_GROUP_CHAT);
        }
        if (user == null) {
            log.error(new EcsEvent(HandlerConstants.CURRENT_USER_NOT_REGISTERED_IN_JIRA_BOT).withContext(CHAT_ID, message.getChat().getId()).withContext(USER_ID, message.getFrom().getId()).withContext(MESSAGE_TEXT, message.getText()).withContext(USER_NAME, message.getFrom().getUserName()));
            return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.CURRENT_USER_NOT_REGISTERED_IN_JIRA_BOT);
        }
        if (Boolean.TRUE.equals(!user.getActive()) || Boolean.TRUE.equals(user.getDeleted())) {
            log.error(new EcsEvent(HandlerConstants.USER_IS_BANNED).withContext(CHAT_ID, message.getChat().getId()).withContext(USER_ID, message.getFrom().getId()).withContext(MESSAGE_TEXT, message.getText()).withContext(USER_NAME, message.getFrom().getUserName()));
            return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.USER_IS_BANNED);
        }

        String title;
        String text = null;
        if (message.getText().contains("|")) {
            title = message.getText().split("\\|")[0].replace(botName, "").trim();
            text = message.getText().split("\\|")[1].trim();
        } else {
            title = message.getText().trim();
        }
        Task task = Task.builder()
                .taskText(text != null ? text : "")
                .taskTitle(title)
                .telegramId(user.getTelegramId())
                .chatId(message.getChatId())
                .taskType(TaskType.GROUP_CHAT_TASK.getValue())
                .project(groupChat.getProject().getKey())
                .status(TaskStatus.CREATED.getValue())
                .build();

        taskService.insertTask(task);
        return null;
    }

    @Override
    public UserState getHandlerName() {
        return UserState.GROUP_CHAT_MESSAGE;
    }
}
