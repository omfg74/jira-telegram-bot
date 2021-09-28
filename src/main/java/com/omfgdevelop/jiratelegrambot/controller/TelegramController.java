package com.omfgdevelop.jiratelegrambot.controller;

import com.omfgdevelop.jiratelegrambot.botapi.JiraBot;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
@Timed
public class TelegramController {

    private final JiraBot jiraBot;

    @PostMapping("/")
    public BotApiMethod<?> onWebHookReceived(@RequestBody() Update update) {
        return jiraBot.onWebhookUpdateReceived(update);
    }
}
