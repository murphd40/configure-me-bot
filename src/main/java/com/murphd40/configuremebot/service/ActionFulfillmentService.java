package com.murphd40.configuremebot.service;

import java.util.List;
import java.util.UUID;

import com.murphd40.configuremebot.actionfulfillment.Action;
import com.murphd40.configuremebot.actionfulfillment.ActionParser;
import com.murphd40.configuremebot.controller.request.webhook.ActionSelectedAnnotationPayload;
import com.murphd40.configuremebot.controller.request.webhook.AnnotationAddedEvent;
import com.murphd40.configuremebot.dao.model.Trigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActionFulfillmentService {

    @Autowired
    private TriggerService triggerService;

    public void handleActionFulfillmentEvents(AnnotationAddedEvent event) {

        boolean actionSelected = event.getAnnotationPayload() instanceof ActionSelectedAnnotationPayload;

        if (!actionSelected) {
            // unsupported annotation type
            return;
        }

        ActionSelectedAnnotationPayload payload = (ActionSelectedAnnotationPayload) event.getAnnotationPayload();

        Action action = ActionParser.parseActionId(payload.getActionId());

        if (action.getType() == null) {
            // unsupported action type
            return;
        }
        
        String spaceId = event.getSpaceId();

        switch (action.getType()) {
            case ADD_TRIGGER:
                String triggerIdString = action.getParams().get(0);
                UUID triggerId = UUID.fromString(triggerIdString);

                boolean success = triggerService.addTriggerToSpace(triggerId, spaceId, event.getUserId());

                if (success) {
                    log.info("Successfully added trigger to space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                } else {
                    log.error("Failed to add trigger to space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                }

                break;
            case DELETE_TRIGGER:
                triggerIdString = action.getParams().get(0);
                triggerId = UUID.fromString(triggerIdString);

                success = triggerService.deleteTriggerFromSpace(triggerId, event.getUserId());

                if (success) {
                    log.info("Successfully removed trigger from space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                } else {
                    log.error("Failed to remove trigger to space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                }
                break;
            case GET_TRIGGERS:
                List<Trigger> triggers = triggerService.getTriggersForSpace(spaceId);

                log.info("Retrieved triggers for space. spaceId = {}, triggers = {}", spaceId, triggers);
                break;
        }

    }

}
