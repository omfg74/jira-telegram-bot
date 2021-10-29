package com.omfgdevelop.jiratelegrambot.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.exception.IssueCreateException;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraIssueService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.omfgdevelop.jiratelegrambot.profiles.Profiles.JIRA_ISSUE_JOB;

@Profile(JIRA_ISSUE_JOB)
@Service
@RequiredArgsConstructor
@Log4j2
public class JiraIssueJob {

    private final JiraIssueService jiraIssueService;

    @Scheduled(fixedDelayString = "${jira.issue.create.delay}", initialDelay = 0)
    public void process() {
        log.info(new EcsEvent("Issue Job started"));
        jiraIssueService.processCreation();
        log.info(new EcsEvent("Issue Job finished"));
    }
}
