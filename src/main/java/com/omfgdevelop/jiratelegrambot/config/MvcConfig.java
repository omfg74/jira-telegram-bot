package com.omfgdevelop.jiratelegrambot.config;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class MvcConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}
