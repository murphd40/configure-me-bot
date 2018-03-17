package com.murphd40.configuremebot.controller.request.webhook;

import lombok.Data;

/**
 * Created by David on 12/02/2018.
 */
@Data
public class MessageCreatedEvent extends AbstractWebhookEvent {

    private String spaceId;
    private String content;

}
