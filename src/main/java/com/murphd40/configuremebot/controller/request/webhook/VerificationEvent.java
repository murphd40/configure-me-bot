package com.murphd40.configuremebot.controller.request.webhook;

import lombok.Data;

/**
 * Created by David on 20/10/2017.
 */
@Data
public class VerificationEvent extends AbstractWebhookEvent {

    private String challenge;

    @Override
    public String getSpaceId() {
        throw new UnsupportedOperationException();
    }
}