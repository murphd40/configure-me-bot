package com.murphd40.configuremebot.client.graphql;

import lombok.NonNull;
import lombok.Value;

@Value
public class Card implements AttachmentPayload {

    @NonNull
    private CardType type;

    @NonNull
    private CardPayload payload;

    public enum CardType {
        INFORMATION
    }

}
