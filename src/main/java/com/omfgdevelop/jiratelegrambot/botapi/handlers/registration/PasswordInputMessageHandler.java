package com.omfgdevelop.jiratelegrambot.botapi.handlers.registration;

import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraLoginService;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.security.auth.login.FailedLoginException;
import javax.ws.rs.core.NoContentException;
import java.security.InvalidKeyException;
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
        User exists = userService.getUserByUserId((long) message.getFrom().getId());
        SendMessage sendMessage = new SendMessage();
        if (exists != null) {
            User user = new User();
            user.setTelegramId((long) message.getFrom().getId());
            user.setJiraPassword(message.getText());
            try {
                userService.createOrUpdate(user);
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
                return new SendMessage(message.getChatId(), "Password encrypt error");
            }

            try {
                jiraLoginService.getMyself(exists.getTelegramId());
            } catch (FailedLoginException loginException) {
                return new SendMessage(message.getChatId(), "LOggin error");
            } catch (NoContentException noContentException) {
                return new SendMessage(message.getChatId(), "Cant fetch user data");
            } catch (Exception e) {
                e.printStackTrace();
            }

            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(String.format("Пароль обновлен для пользователя %s.", exists.getJiraUsername()));
            registeredUserSet.add(Long.valueOf(message.getFrom().getId()));
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.STAND_BY);
        } else {
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Пользователь не найден");
            log.error(new EcsEvent("User not found").withContext("user_id",message.getChat()));
        }
        return sendMessage;
    }


    @Override
    public UserState getHandlerName() {
        return UserState.NEW_JIRA_PASSWORD;
    }
}
