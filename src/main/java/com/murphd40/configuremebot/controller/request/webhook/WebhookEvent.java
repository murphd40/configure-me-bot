package com.murphd40.configuremebot.controller.request.webhook;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by David on 12/02/2018.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AnnotationAddedEvent.class, name = "message-annotation-added"),
    @JsonSubTypes.Type(value = VerificationEvent.class, name = "verification"),
    @JsonSubTypes.Type(value = MessageCreatedEvent.class, name = "message-created"),
    @JsonSubTypes.Type(value = MembersAddedEvent.class, name = "space-members-added")
})
public interface WebhookEvent {

    String getType();

    String getSpaceId();

    String getUserId();

}
