package com.omfgdevelop.jiratelegrambot.botapi;

public enum UserState {
    UNREGISTERED(0),
    NEW_TASK_TITLE(1),
    NEW_TASK_TEXT(2),
    NEW_USER_NAME(3),
    NEW_JIRA_PASSWORD(4),
    CANCELLING_CURRENT_TASK(5),
    CREATING_NEW_TASK(6),
    STAND_BY(7),
    PROJECT_SELECT(8);

    private final Integer value;

    UserState(int i) {
        value = i;
    }

    public Integer getValue() {
        return value;
    }
}