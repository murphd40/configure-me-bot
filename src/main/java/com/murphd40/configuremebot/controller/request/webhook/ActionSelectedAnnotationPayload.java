package com.murphd40.configuremebot.controller.request.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionSelectedAnnotationPayload extends AnnotationPayload {

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
