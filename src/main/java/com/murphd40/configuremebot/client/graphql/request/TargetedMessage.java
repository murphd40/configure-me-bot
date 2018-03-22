package com.murphd40.configuremebot.client.graphql.request;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TargetedMessage {

    @NonNull
    private String conversationId;
    @NonNull
    private String targetUserId;
    @NonNull
    private String targetDialogId;

    private List<Attachment> attachments;
    private List<AnnotationWrapper> annotations;

}
