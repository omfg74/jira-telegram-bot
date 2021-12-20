package com.omfgdevelop.jiratelegrambot.enums;

public enum TaskType {

    PRIVATE_TASK(0), GROUP_CHAT_TASK(1);

    private final Integer value;

    TaskType(int typeId) {
        this.value = typeId;
    }

    public Integer getValue() {
        return value;
    }
}
