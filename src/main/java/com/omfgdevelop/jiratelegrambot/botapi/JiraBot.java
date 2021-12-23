package com.omfgdevelop.jiratelegrambot.botapi;

import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.TelegramMessageProcessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
public class JiraBot extends TelegramWebhookBot {

    private final TelegramMessageProcessor telegramMessageProcessor;

    @Value("${app.telegrambot.token}")
    private String token;

    @Value("${app.telegram.username}")
    private String username;

    @Value("${app.webhook.address}")
    private String webhookAddress;

    public JiraBot(DefaultBotOptions options, TelegramMessageProcessor telegramMessageProcessor) {
        super(options);
        this.telegramMessageProcessor = telegramMessageProcessor;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() == null&& !update.hasCallbackQuery()) {
            log.error(new EcsEvent("Null message"));
            return null;
        }
        BotApiMethod message = telegramMessageProcessor.handleMessage(update);
        return message;
    }

    @Override
    public String getBotPath() {
        return webhookAddress;
    }
}
