package com.omfgdevelop.jiratelegrambot.botapi.handlers.registration;

import com.omfgdevelop.jiratelegrambot.HandlerConstants;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraLoginService;
import com.omfgdevelop.jiratelegrambot.view.jira.auth.Myself;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.security.auth.login.FailedLoginException;
import javax.ws.rs.core.NoContentException;
import java.util.Set;

import static com.omfgdevelop.jiratelegrambot.config.AppConfig.REGISTERED_USER_SET;

@Component
@RequiredArgsConstructor
@Log4j2
public class PasswordInputMessageHandler implements MessageHandler {

    private final UserService userService;

    @Qualifier(REGISTERED_USER_SET)
    private final Set<Long> registeredUserSet;

    private final UserStateCache userStateCache;

    private final JiraLoginService jiraLoginService;


    @Override
    public SendMessage handleInputMessage(Message message) {
        User exists = userService.getUserByTelegramId((long) message.getFrom().getId());
        SendMessage sendMessage = new SendMessage();
        if (exists != null) {
            Myself myself = null;
            try {
                myself = jiraLoginService.getMyself(exists.getTelegramId(), message.getText());
            } catch (FailedLoginException loginException) {
                log.error(new EcsEvent(String.format("Jira password check failed for user %s", exists.getJiraUsername())).with(loginException).withContext("user_name", exists.getJiraUsername()));
                userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_JIRA_PASSWORD);
                return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.LOGIN_ERROR);
            } catch (NoContentException noContentException) {
                log.error(new EcsEvent(String.format("Jira password check failed for user %s", exists.getJiraUsername())).with(noContentException).withContext("user_name", exists.getJiraUsername()));
                userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_JIRA_PASSWORD);
                return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.CANT_FETCH_USER_DATA);
            } catch (Exception e) {
                log.error(new EcsEvent(String.format("Jira password check failed for user %s", exists.getJiraUsername())).with(e).withContext("user_name", exists.getJiraUsername()));
                userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_JIRA_PASSWORD);
                return new SendMessage(String.valueOf(message.getChatId()), HandlerConstants.JIRA_PASSWORD_CHECK_FAILED);
            }

            if (myself != null && myself.getActive()) {
                User user = userService.getUserByTelegramId((long) message.getFrom().getId());
                user.setPasswordApproved(true);
                user.setDeleted(false);
                userService.updateUser(user);
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(String.format(HandlerConstants.REGISTRATION_SUCCESS, exists.getJiraUsername()));
                registeredUserSet.add(Long.valueOf(message.getFrom().getId()));
                userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.STAND_BY);
            } else {
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(String.format(HandlerConstants.USER_BLOCKED_IN_JIRA, exists.getJiraUsername()));
                registeredUserSet.remove(Long.valueOf(message.getFrom().getId()));
                userService.deleteUser(Long.valueOf(message.getFrom().getId()));
                userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.UNREGISTERED);
            }
        } else {
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText("Пользователь не найден");
            log.error(new EcsEvent("User not found").withContext("user_id", message.getChat()));
        }
        return sendMessage;
    }


    @Override
    public UserState getHandlerName() {
        return UserState.NEW_JIRA_PASSWORD;
    }
}
