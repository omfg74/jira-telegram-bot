package com.omfgdevelop.jiratelegrambot.job;

import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.JiraProjectFetchJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.omfgdevelop.jiratelegrambot.profiles.Profiles.JIRA_UPDATE_PROFILE;

@Service
@RequiredArgsConstructor
@Profile(JIRA_UPDATE_PROFILE)
@Log4j2
public class JiraDataUpdateService {

    @Value("${jira.admin.username}")
    private final String adminUsername;

    @Value("${jira.admin.password}")
    private final String adminPassword;

    private final JiraProjectFetchJob projectService;

    @Scheduled(fixedDelayString = "${jira.update.delay}", initialDelay = 0)
    public void process() {
        try {
            log.debug(new EcsEvent("Projects list update started"));
            projectService.getJiraProfiles(adminUsername, adminPassword);
        } catch (Exception e) {
            log.error(new EcsEvent("Update project list").with(e));
        }
    }
}
