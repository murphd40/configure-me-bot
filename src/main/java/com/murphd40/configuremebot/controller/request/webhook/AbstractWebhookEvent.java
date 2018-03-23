package com.murphd40.configuremebot.controller.request.webhook;

import lombok.Data;

/**
 * Created by David on 20/10/2017.
 */
@Data
abstract class AbstractWebhookEvent implements WebhookEvent {

    private String type;
    private String userId;
    private String spaceId;

}
