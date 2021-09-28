package com.omfgdevelop.jiratelegrambot.enums;

public enum TaskType {

    NEW_TASK(0);

    private final Integer value;

    TaskType(int typeId) {
        this.value = typeId;
    }

    public Integer getValue() {
        return value;
    }
}
