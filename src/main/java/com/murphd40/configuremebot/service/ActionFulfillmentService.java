package com.murphd40.configuremebot.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.murphd40.configuremebot.actionfulfillment.Action;
import com.murphd40.configuremebot.actionfulfillment.ActionParser;
import com.murphd40.configuremebot.actionfulfillment.ActionType;
import com.murphd40.configuremebot.client.graphql.request.AnnotationWrapper;
import com.murphd40.configuremebot.client.graphql.request.Attachment;
import com.murphd40.configuremebot.client.graphql.request.Card;
import com.murphd40.configuremebot.client.graphql.request.GenericAnnotation;
import com.murphd40.configuremebot.client.graphql.request.InformationCard;
import com.murphd40.configuremebot.client.graphql.request.InformationCard.Button;
import com.murphd40.configuremebot.client.graphql.request.TargetedMessage;
import com.murphd40.configuremebot.client.graphql.response.Person;
import com.murphd40.configuremebot.controller.request.webhook.ActionSelectedAnnotationPayload;
import com.murphd40.configuremebot.controller.request.webhook.AnnotationAddedEvent;
import com.murphd40.configuremebot.dao.model.Trigger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ActionFulfillmentService {

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private WatsonWorkService watsonWorkService;

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

                success = triggerService.deleteTriggerFromSpace(triggerId, event.getSpaceId());

                if (success) {
                    log.info("Successfully removed trigger from space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                } else {
                    log.error("Failed to remove trigger to space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                }
                break;
            case GET_TRIGGERS:
                List<Trigger> triggers = triggerService.getTriggersForSpace(spaceId);
                log.info("Retrieved triggers for space. spaceId = {}, triggers = {}", spaceId, triggers);

                List<String> creatorIds = triggers.stream().map(Trigger::getCreatorId).collect(Collectors.toList());
                List<Person> people = watsonWorkService.getPeople(creatorIds);
                Map<String, Person> peopleById = CollectionUtils.emptyIfNull(people).stream()
                    .collect(Collectors.toMap(Person::getId, Function.identity()));

                List<Attachment> attachments = triggers.stream()
                    .map(trigger -> createCardForTrigger(trigger, peopleById.get(trigger.getCreatorId())))
                    .collect(Collectors.toList());

                watsonWorkService.sendTargetedMessage(buildTargetedMessageWithAttachments(event, attachments));
                break;
            case TRIGGER_INFO:
                triggerIdString = action.getParams().get(0);
                triggerId = UUID.fromString(triggerIdString);

                Trigger trigger = triggerService.findTrigger(spaceId, triggerId);

                AnnotationWrapper annotation = createAnnotationForTrigger(trigger);

                watsonWorkService.sendTargetedMessage(buildTargetedMessageWithAnnotations(event, Collections.singletonList(annotation)));
        }

    }

    private AnnotationWrapper createAnnotationForTrigger(Trigger trigger) {

        StringBuilder builder = new StringBuilder();

        if (StringUtils.hasText(trigger.getCondition())) {
            builder.append(String.format("*Condition:* `%s`", trigger.getCondition())).append('\n');
        }

        builder.append(String.format("*Action:* `%s`", trigger.getAction()));

        GenericAnnotation annotation = GenericAnnotation.builder()
            .text(builder.toString())
            .title(trigger.getTitle())
            .build();

        return new AnnotationWrapper(annotation);
    }

    private Attachment createCardForTrigger(Trigger trigger, Person creator) {

        String body = String.format("Created by %s", creator.getDisplayName());

        String buttonPayload = String.format("%s %s", ActionType.TRIGGER_INFO.getActionId(), trigger.getTriggerId());
        Button button = new Button(Button.ButtonStyle.PRIMARY, "See more", buttonPayload);

        InformationCard payload = InformationCard.builder()
            .date(trigger.getTriggerId().timestamp())
            .title("Trigger")
            .subtitle(trigger.getTitle())
            .text(body)
            .buttons(Collections.singletonList(button))
            .build();

        return new Attachment(Attachment.AttachmentType.CARD, new Card(Card.CardType.INFORMATION, payload));
    }

    private TargetedMessage buildTargetedMessageWithAttachments(AnnotationAddedEvent event, List<Attachment> attachments) {
        return buildTargetedMessage(event, attachments, Collections.emptyList());
    }

    private TargetedMessage buildTargetedMessageWithAnnotations(AnnotationAddedEvent event, List<AnnotationWrapper> annotations) {
        return buildTargetedMessage(event, Collections.emptyList(), annotations);
    }

    private TargetedMessage buildTargetedMessage(AnnotationAddedEvent event, List<Attachment> attachments, List<AnnotationWrapper> annotations) {
        ActionSelectedAnnotationPayload payload = (ActionSelectedAnnotationPayload) event.getAnnotationPayload();
        return TargetedMessage.builder()
            .conversationId(event.getSpaceId())
            .targetDialogId(payload.getTargetDialogId())
            .targetUserId(event.getUserId())
            .annotations(annotations)
            .attachments(attachments).build();
    }

}
