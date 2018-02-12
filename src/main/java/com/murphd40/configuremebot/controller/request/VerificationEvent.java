package com.murphd40.configuremebot.controller.request;

import lombok.Data;

/**
 * Created by David on 20/10/2017.
 */
@Data
public class VerificationEvent extends AbstractWebhookEvent {

    private String challenge;

}
