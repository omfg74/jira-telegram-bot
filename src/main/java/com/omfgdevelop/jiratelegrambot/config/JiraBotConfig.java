package com.omfgdevelop.jiratelegrambot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class JiraBotConfig {

    @Value("${app.webhook.address}")
    private String webHookPath;
    @Value("${app.telegrambot.token}")
    private String botToken;
    @Value("${app.telegram.username}")
    private String userName;

}
