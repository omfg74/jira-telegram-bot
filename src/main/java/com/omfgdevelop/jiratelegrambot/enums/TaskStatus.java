package com.omfgdevelop.jiratelegrambot.enums;

public enum TaskStatus {
    CREATED(0),
    PROCESSING(1),
    ERROR(2),
    DONE(3),
    TITLE_INPUT(4),
    TEXT_INPUT(5),
    PROJECT_INPUT(6);

    private final Integer value;

    TaskStatus(int status) {
        this.value = status;
    }

    public Integer getValue() {
        return value;
    }
}
