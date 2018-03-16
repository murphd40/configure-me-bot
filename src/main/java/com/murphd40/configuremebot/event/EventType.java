package com.murphd40.configuremebot.event;

import java.util.Arrays;

public enum EventType {
    MESSAGE_CREATED("message-created"),
    MESSAGE_ANNOTATION_ADDED("message-annotation-added"),
    MEMBERS_ADDED("members-added");

    private String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EventType fromString(String value) {
        return Arrays.stream(values()).filter(eventType -> eventType.value.equals(value)).findFirst().orElse(null);
    }
}
