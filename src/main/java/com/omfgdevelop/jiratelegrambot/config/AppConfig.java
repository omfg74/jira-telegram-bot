package com.omfgdevelop.jiratelegrambot.config;

import com.omfgdevelop.jiratelegrambot.botapi.JiraBot;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.repository.UserRepository;
import com.omfgdevelop.jiratelegrambot.service.TelegramMessageProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    public static final String REGISTERED_USER_SET = "registers_users";
    public static final String ACTIVE_SESSION = "active_session";

    @Value("${app.password.secret}")
    public final String secret;
    @Value("${app.password.salt}")
    public final String salt;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    @Bean
    public JiraBot getJiraBot(TelegramMessageProcessor messageProcessor) {
        DefaultBotOptions options = new DefaultBotOptions();
        return new JiraBot(options, messageProcessor);
    }

    @Bean(REGISTERED_USER_SET)
    public Set<Long> getConcurrentHashSet() {
        List<User> users = userRepository.findAllWithUsernameNotNullAndPasswordNotNullAndActiveIsTrue();

        return users.stream().sequential().map(User::getTelegramId).collect(Collectors.toSet());
    }

    @Bean(ACTIVE_SESSION)
    public ConcurrentHashMap<Long, String> getActiveSession() {
        return new ConcurrentHashMap<>();
    }


    @Bean
    public LinkedBlockingDeque<Task> getQueue() {
        return new LinkedBlockingDeque<>();
    }

    @Bean
    public Cipher getChipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES");
    }

    @Bean
    public SecretKey keyGenerator() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
