package com.murphd40.configuremebot.controller.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * Created by David on 20/10/2017.
 */
@Data
public class AnnotationAddedEvent extends AbstractWebhookEvent {

    private String spaceName;
    private String spaceId;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "annotationType")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = MessageFocusAnnotationPayload.class, name = "message-focus")
    })
    private AnnotationPayload annotationPayload;
//    private String annotationPayload;
    private String messageId;
    private String annotationType;
    private String annotationId;
    private String userId;
    private Long time;

}