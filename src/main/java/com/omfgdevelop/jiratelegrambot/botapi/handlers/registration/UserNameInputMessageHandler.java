package com.omfgdevelop.jiratelegrambot.botapi.handlers.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omfgdevelop.jiratelegrambot.botapi.UserState;
import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import com.omfgdevelop.jiratelegrambot.view.jira.JiraUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

import static com.omfgdevelop.jiratelegrambot.Common.createHeaders;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserNameInputMessageHandler implements MessageHandler {

    private final UserService userService;

    private final UserStateCache userStateCache;

    private final RestTemplate restTemplate;

    @Value("${jira.base.url}")
    private String baseUrl;

    @Value("${jira.admin.username}")
    private String jiraAdminUsername;

    @Value("${jira.admin.password}")
    private String jiraAdminPassword;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SendMessage handleInputMessage(Message message) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        User exists = userService.getUserByTelegramId((long) message.getFrom().getId());
        SendMessage sendMessage = new SendMessage();
        if (exists == null) {
            User user = new User();
            user.setTelegramId((long) message.getFrom().getId());
            user.setJiraUsername(message.getText().trim());
            user.setTelegramUsername(message.getFrom().getFirstName());
            boolean banned = userService.createOrUpdate(user);
            if (banned) {
                log.warn(new EcsEvent("Попытка регистрации заблокированного пользователя").withContext("user_name", user.getJiraUsername()));
                return new SendMessage(String.valueOf(message.getChatId()), String.format("Пользователь %s заблокирован", user.getJiraUsername()));
            }

            return provideUser(user, sendMessage, message);
        } else if (Boolean.TRUE.equals(exists.getDeleted())) {
            if (Boolean.FALSE.equals(exists.getActive())) {
                log.warn(new EcsEvent("Попытка регистрации заблокированного пользователя").withContext("user_name", exists.getJiraUsername()));
                return new SendMessage(String.valueOf(message.getChatId()), String.format("Пользователь %s заблокирован", exists.getJiraUsername()));
            }
            return provideUser(exists, sendMessage, message);
        } else {
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_JIRA_PASSWORD);
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText("На этот telegramId уже назначен пользователь Jira. Введите пароль от вашего пользователя или введите /delete_user для отмены привязки");
        }
        return sendMessage;
    }

    private SendMessage provideUser(User user, SendMessage sendMessage, Message message) {
        if (fetchUserDataFromJira(user)) {
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText(String.format("Добавлен пользователь %s. Введите пароль. Он не будет сохранен, будет использоваться только для подтверждения входа в jira.", message.getText()));
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_JIRA_PASSWORD);
        } else {
            userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.UNREGISTERED);
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText(String.format("Пользователя %s не существует в Jira. Если имя введено не верно, то введите /delete_user и повторите авторизацию", message.getText()));
        }

        return sendMessage;
    }

    private boolean fetchUserDataFromJira(User user) {
        try {

            User entity = userService.getUserByTelegramId(user.getTelegramId());
            HttpHeaders headers = createHeaders(jiraAdminUsername, jiraAdminPassword);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + "/rest/api/2/user?username=" + user.getJiraUsername(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                JiraUserInfo userInfo = objectMapper.readValue(responseEntity.getBody(), JiraUserInfo.class);
                user.setId(entity.getId());
                user.setDisplayName(userInfo.getDisplayName());
                user.setEmailAddress(userInfo.getEmailAddress());
                user.setSelf(userInfo.getSelf());
                user.setActive(userInfo.getActive());
                userService.updateUser(user);
                log.info(new EcsEvent("User found in jira").withContext("user_name", user.getJiraUsername()));
                return true;
            }
            log.warn(new EcsEvent("User not found in jira").withContext("user_name", user.getJiraUsername()));

            return false;
        } catch (Exception e) {
            log.error(new EcsEvent("Error fetching userdata from jira").with(e).withContext("user_name", user.getJiraUsername()));
            return false;
        }
    }

    @Override
    public UserState getHandlerName() {
        return UserState.NEW_USER_NAME;
    }
}
