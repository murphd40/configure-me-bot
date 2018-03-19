package com.murphd40.configuremebot.controller.request.webhook;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionSelectedAnnotationPayload extends AnnotationPayload {

    @JsonCreator
    public static ActionSelectedAnnotationPayload create(String json) throws IOException {
        return cleanJsonAndCreate(json, ActionSelectedAnnotationPayload.class);
    }

    private String type;
    private String annotationId;
    private String version;
    private long created;
    private String createdBy;
    private long updated;
    private String updatedBy;
    private String tokenClientId;
    private String conversationId;
    private String targetDialogId;
    private String referralMessageId;
    // for slash commands it will take the form of: '/actionName param1 param2'
    private String actionId;
    private String targetAppId;

}
