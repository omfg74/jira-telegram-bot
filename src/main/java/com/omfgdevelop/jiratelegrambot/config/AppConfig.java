package com.omfgdevelop.jiratelegrambot.config;

import com.omfgdevelop.jiratelegrambot.botapi.JiraBot;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.repository.UserRepository;
import com.omfgdevelop.jiratelegrambot.service.TelegramMessageProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    public static final String REGISTERED_USER_SET = "registers_users";

    private Set<Long> registeredSet;


    @Bean
    public JiraBot getJiraBot(TelegramMessageProcessor messageProcessor) {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        return new JiraBot(options, messageProcessor);
    }

    @Bean(REGISTERED_USER_SET)
    public Set<Long> getConcurrentHashSet() {
        List<User> users = userRepository.findAllWithUsernameNotNullAndPasswordNotNull();

        return users.stream().sequential().map(User::getTelegramId).collect(Collectors.toSet());
    }

}
