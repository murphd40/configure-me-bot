package com.murphd40.configuremebot.client.graphql.request;

import lombok.NonNull;
import lombok.Value;

@Value
public class Attachment {

    @NonNull
    private AttachmentType type;

    @NonNull
    private AttachmentPayload payload;

    public enum AttachmentType {
        CARD
    }

}
