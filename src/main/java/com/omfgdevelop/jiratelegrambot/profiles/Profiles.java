package com.omfgdevelop.jiratelegrambot.profiles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Profiles {

    public static final String TELEGRAM_FETCH_PROFILE = "telegram";
    public static final String JIRA_UPDATE_PROFILE = "jira_update";
    public static final String JIRA_ISSUE_JOB = "jira_issue";
}
