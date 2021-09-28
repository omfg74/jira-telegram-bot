package com.omfgdevelop.jiratelegrambot.botapi;

import com.omfgdevelop.jiratelegrambot.service.TelegramMessageProcessor;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class JiraBot extends TelegramWebhookBot {

    private final TelegramMessageProcessor telegramMessageProcessor;

    public JiraBot(DefaultBotOptions options, TelegramMessageProcessor telegramMessageProcessor) {
        super(options);
        this.telegramMessageProcessor = telegramMessageProcessor;
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return null;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        SendMessage message = telegramMessageProcessor.handleMessage(update);
        return message;
    }

    @Override
    public String getBotPath() {
        return null;
    }
}
