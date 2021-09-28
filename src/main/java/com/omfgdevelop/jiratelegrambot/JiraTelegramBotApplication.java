package com.omfgdevelop.jiratelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class JiraTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiraTelegramBotApplication.class, args);
	}

}
