package com.murphd40.configuremebot.event;

import java.util.Arrays;

import com.murphd40.configuremebot.controller.request.webhook.AnnotationAddedEvent;
import com.murphd40.configuremebot.controller.request.webhook.MessageCreatedEvent;
import com.murphd40.configuremebot.controller.request.webhook.WebhookEvent;

public enum EventType {
    MESSAGE_CREATED("message-created", MessageCreatedEvent.class),
    MESSAGE_ANNOTATION_ADDED("message-annotation-added", AnnotationAddedEvent.class);
//    MEMBERS_ADDED("members-added");

    private String value;

    private Class<? extends WebhookEvent> eventClass;

    EventType(String value, Class<? extends WebhookEvent> eventClass) {
        this.value = value;
        this.eventClass = eventClass;
    }

    public String getValue() {
        return value;
    }

    public Class<? extends WebhookEvent> getEventClass() {
        return eventClass;
    }

    public static EventType fromString(String value) {
        return Arrays.stream(values()).filter(eventType -> eventType.value.equals(value)).findFirst().orElse(null);
    }
}
