package com.omfgdevelop.jiratelegrambot;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Common {

    public static HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("cache-control", "no-cache");
            set("X-Atlassian-Token", "nocheck");
            set("Authorization", authHeader);
        }};
    }

    public static SendMessage createProjectMenu(Message message, Task task, JiraProjectService projectService) {
        List<ProjectEntity> projectList = projectService.getPermittedProjects();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            if (i % 2 == 0) {
                keyboard.add(row);
                row = new ArrayList<>();
            }
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(projectList.get(i).getName());
            inlineKeyboardButton.setCallbackData(task.getTaskTitle() + "/" + projectList.get(i).getKey());
            row.add(inlineKeyboardButton);
        }
        keyboard.add(row);

        markup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage(String.valueOf(message.getChatId()), String.format("К какому проекту добавить задачу %s?", task.getTaskTitle()));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }
}
