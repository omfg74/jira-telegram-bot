package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.meta.api.methods.updates.GetWebhookInfo;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.WebhookInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Service
@RequiredArgsConstructor
@Log4j2
public class WebhookControlService {

    private final RestTemplate restTemplate;

    @Value("${app.webhook.address}")
    private String webhookAddress;

    @Value("${app.telegrambot.token}")
    private String token;

    @Value("${app.telegrambot.url}")
    private String botUrl;


    public void checkAndFix() throws TelegramApiRequestException {
        HttpHeaders headers = new HttpHeaders();
        GetWebhookInfo getWebhookInfo = new GetWebhookInfo();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(botUrl + token + "/" + GetWebhookInfo.PATH);
        ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        WebhookInfo webhookInfo = getWebhookInfo.deserializeResponse(responseEntity.getBody());
        String url = webhookInfo.getUrl();
        if (!url.equalsIgnoreCase(webhookAddress)) {
            log.warn(() -> new EcsEvent("Webhook was changed").withContext("previous_webhook", url).withContext("current_webhook", webhookAddress));
            updateWebhookInfo();
        }
    }

    private void updateWebhookInfo() {
        HttpHeaders headers = new HttpHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(botUrl + token + "/" + SetWebhook.PATH + "?" + SetWebhook.URL_FIELD + "=" + webhookAddress);
        ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

    }
}
