package com.omfgdevelop.jiratelegrambot.job;


import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.WebhookControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.omfgdevelop.jiratelegrambot.profiles.Profiles.TELEGRAM_WEBHOOK_CHECK;

@Profile(TELEGRAM_WEBHOOK_CHECK)
@Service
@RequiredArgsConstructor
@Log4j2
public class WebhookCheckJob {

    private final WebhookControlService webhookControlService;

    @Scheduled(fixedDelayString = "${jira.webhook.update.delay}", initialDelay = 0)
    public void process() {
        try {
            log.debug(() -> new EcsEvent("Webhook check started "));
            webhookControlService.checkAndFix();
            log.debug(() -> new EcsEvent("Webhook check stopped"));
        } catch (Exception e) {
            log.error(new EcsEvent("WebHook check error").with(e).withContext("WebHook check error message", e.getMessage()));
        }
    }
}
