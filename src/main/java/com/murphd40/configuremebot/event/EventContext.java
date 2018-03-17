package com.murphd40.configuremebot.event;

import com.murphd40.configuremebot.controller.request.webhook.WebhookEvent;
import lombok.Value;

@Value
public class EventContext {

    private TriggerActions actions;
    private WebhookEvent event;

}
