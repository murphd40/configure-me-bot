package com.murphd40.configuremebot.controller;

import com.murphd40.configuremebot.configuration.WatsonWorkspaceProperties;
import com.murphd40.configuremebot.controller.request.webhook.AnnotationAddedEvent;
import com.murphd40.configuremebot.controller.request.webhook.VerificationEvent;
import com.murphd40.configuremebot.controller.request.webhook.WebhookEvent;
import com.murphd40.configuremebot.controller.response.VerificationResponse;
import com.murphd40.configuremebot.service.ActionFulfillmentService;
import com.murphd40.configuremebot.service.AuthService;
import com.murphd40.configuremebot.service.EventHandlerService;
import com.murphd40.configuremebot.service.WatsonWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

/**
 * Created by David on 11/02/2018.
 */
@Controller
public class WebhookController {

    @Autowired
    private AuthService authService;
    @Autowired
    private WatsonWorkService watsonWorkService;
    @Autowired
    private EventHandlerService eventHandlerService;
    @Autowired
    private ActionFulfillmentService actionFulfillmentService;
    @Autowired
    private WatsonWorkspaceProperties watsonWorkspaceProperties;

    @PostMapping(path = "/webhook")
    public ResponseEntity<?> webhook(@RequestHeader("X-OUTBOUND-TOKEN") String outboundToken, @RequestBody WebhookEvent webhookEvent) {
        boolean isVerificationEvent = webhookEvent instanceof VerificationEvent;
        return isVerificationEvent ? processVerificationRequest((VerificationEvent) webhookEvent, outboundToken)
            : processWebhookEvent(webhookEvent);
    }

    private ResponseEntity<?> processVerificationRequest(VerificationEvent verificationEvent, String outboundToken) {
        if (!authService.isValidVerificationRequest(verificationEvent, outboundToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        VerificationResponse responseBody = new VerificationResponse(verificationEvent.getChallenge());
        return ResponseEntity.ok()
            .header("X-OUTBOUND-TOKEN", authService.createVerificationHeader(responseBody))
            .body(responseBody);
    }

    private ResponseEntity<?> processWebhookEvent(WebhookEvent webhookEvent) {

        Mono.just(webhookEvent)
            .filter(event -> !watsonWorkspaceProperties.getApp().getId().equals(event.getUserId()))
            .filter(AnnotationAddedEvent.class::isInstance)
            .cast(AnnotationAddedEvent.class)
            .doOnNext(actionFulfillmentService::handleActionFulfillmentEvents)
            .block();

        Mono.just(webhookEvent)
            .filter(event -> !watsonWorkspaceProperties.getApp().getId().equals(event.getUserId()))
            .doOnNext(eventHandlerService::processWebhookEvent)
            .subscribe();

        return ResponseEntity.ok().build();
    }

}
