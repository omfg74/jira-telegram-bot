package com.omfgdevelop.jiratelegrambot.profiles;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile(Profiles.TELEGRAM_FETCH_PROFILE)
public class TelegramFetchProfile {

    @Scheduled(fixedDelayString = "app.telegram.refresh.timeout")
    public void process(){}

}
