package com.murphd40.configuremebot.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.datastax.driver.core.utils.UUIDs;
import com.murphd40.configuremebot.actionfulfillment.Action;
import com.murphd40.configuremebot.actionfulfillment.ActionParser;
import com.murphd40.configuremebot.actionfulfillment.ActionType;
import com.murphd40.configuremebot.client.graphql.request.AnnotationWrapper;
import com.murphd40.configuremebot.client.graphql.request.Attachment;
import com.murphd40.configuremebot.client.graphql.request.Card;
import com.murphd40.configuremebot.client.graphql.request.GenericAnnotation;
import com.murphd40.configuremebot.client.graphql.request.GenericAnnotation.Button.PostbackButton;
import com.murphd40.configuremebot.client.graphql.request.InformationCard;
import com.murphd40.configuremebot.client.graphql.request.InformationCard.Button;
import com.murphd40.configuremebot.client.graphql.request.Message;
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

    public static final String BLANK_LINE = "\\u200B\\n";
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

        AnnotationWrapper annotation;
        switch (action.getType()) {
            case ADD_TRIGGER:
                String triggerIdString = action.getParams().get(0);
                UUID triggerId = UUID.fromString(triggerIdString);

                Trigger trigger = triggerService.addTriggerToSpace(triggerId, spaceId, event.getUserId());
                boolean success = trigger != null;

                StringBuilder stringBuilder = new StringBuilder();
                if (success) {
                    log.info("Successfully added trigger to space. triggerId = {}, spaceId = {}", triggerId, spaceId);

                    stringBuilder.append(String.format("*Title:* %s", trigger.getTitle())).append("\\n");
                    stringBuilder.append(String.format("*Event type:* %s", trigger.getEventType())).append("\\n");

                    if (StringUtils.hasText(trigger.getCondition())) {
                        stringBuilder
                            .append("*Condition:*").append("\\n")
                            .append("```").append("\\n")
                            .append(trigger.getCondition()).append("\\n")
                            .append("```").append("\\n");
                    }

                    stringBuilder
                        .append("*Action:*").append("\\n")
                        .append("```").append("\\n")
                        .append(trigger.getAction()).append("\\n")
                        .append("```");

                    annotation = new AnnotationWrapper(GenericAnnotation.builder()
                        .actor(new GenericAnnotation.Actor(event.getUserName()))
                        .title("added a new trigger")
                        .text(stringBuilder.toString())
                        .build());

                    Message message = Message.builder()
                        .conversationId(spaceId)
                        .annotations(Collections.singletonList(annotation))
                        .build();

                    watsonWorkService.sendMessage(message);

                    stringBuilder = new StringBuilder();
                    stringBuilder.append(BLANK_LINE)
                        .append("*Success!*").append("\\n")
                        .append(BLANK_LINE)
                        .append(String.format("'%s' has been added to the space", trigger.getTitle()));

                } else {
                    log.error("Failed to add trigger to space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                    stringBuilder.append(BLANK_LINE)
                        .append("Failed to add trigger to space");
                }

                annotation = new AnnotationWrapper(GenericAnnotation.builder().text(stringBuilder.toString()).build());
                watsonWorkService.sendTargetedMessage(buildTargetedMessageWithAnnotations(event, Collections.singletonList(annotation)));

                break;
            case DELETE_TRIGGER:
                triggerIdString = action.getParams().get(0);
                triggerId = UUID.fromString(triggerIdString);

                trigger = triggerService.findTrigger(spaceId, triggerId);

                triggerService.deleteTriggerFromSpace(triggerId, event.getSpaceId());

                stringBuilder = new StringBuilder();
                if (trigger != null) {
                    log.info("Successfully removed trigger from space. triggerId = {}, spaceId = {}", triggerId, spaceId);

                    annotation = new AnnotationWrapper(GenericAnnotation.builder()
                        .actor(new GenericAnnotation.Actor(event.getUserName()))
                        .title("removed a trigger")
                        .text(String.format("*Title:* %s", trigger.getTitle()))
                        .build());

                    Message message = Message.builder()
                        .conversationId(spaceId)
                        .annotations(Collections.singletonList(annotation))
                        .build();

                    watsonWorkService.sendMessage(message);

                    stringBuilder.append(BLANK_LINE)
                        .append("*Success!*").append("\\n")
                        .append(BLANK_LINE)
                        .append(String.format("'%s' has been removed from the space", trigger.getTitle()));
                } else {
                    log.error("Failed to remove trigger to space. triggerId = {}, spaceId = {}", triggerId, spaceId);
                    stringBuilder.append(BLANK_LINE).append("Failed to remove trigger from space");
                }

                annotation = new AnnotationWrapper(
                    GenericAnnotation.builder().text(stringBuilder.toString()).build());

                watsonWorkService.sendTargetedMessage(buildTargetedMessageWithAnnotations(event, Collections.singletonList(annotation)));

                break;
            case GET_TRIGGERS:
                List<Trigger> triggers = triggerService.getTriggersForSpace(spaceId);
                log.info("Retrieved triggers for space. spaceId = {}, triggers = {}", spaceId, triggers);

                List<String> creatorIds = triggers.stream().map(Trigger::getCreatorId).collect(Collectors.toList());
                List<Person> people = watsonWorkService.getPeople(creatorIds);
                Map<String, Person> peopleById = CollectionUtils.emptyIfNull(people).stream()
                    .collect(Collectors.toMap(Person::getId, Function.identity()));

                List<Attachment> attachments = triggers.stream()
                    .map(t -> createCardForTrigger(t, peopleById.get(t.getCreatorId())))
                    .collect(Collectors.toList());

                watsonWorkService.sendTargetedMessage(buildTargetedMessageWithAttachments(event, attachments));

                break;
            case TRIGGER_INFO:
                triggerIdString = action.getParams().get(0);
                triggerId = UUID.fromString(triggerIdString);

                trigger = triggerService.findTrigger(spaceId, triggerId);

                Person creator = watsonWorkService.getPeople(Collections.singletonList(trigger.getCreatorId())).get(0);

                annotation = createAnnotationForTrigger(trigger, creator);

                watsonWorkService.sendTargetedMessage(buildTargetedMessageWithAnnotations(event, Collections.singletonList(annotation)));
        }

    }

    private AnnotationWrapper createAnnotationForTrigger(Trigger trigger, Person creator) {

        StringBuilder builder = new StringBuilder("\\u200B \\n");

        builder.append(String.format("*Added by:* %s", creator.getDisplayName())).append("\\n \\u200B \\n");
        builder.append(String.format("*Event type:* %s", trigger.getEventType())).append("\\n \\u200B \\n");

        if (StringUtils.hasText(trigger.getCondition())) {
            builder
                .append("*Condition:*").append("\\n")
                .append("```").append("\\n")
                .append(trigger.getCondition()).append("\\n")
                .append("```").append("\\n");
        }

        builder
            .append("*Action:*").append("\\n")
            .append("```").append("\\n")
            .append(trigger.getAction()).append("\\n")
            .append("```").append("\\n");

        PostbackButton deleteButton = new PostbackButton(PostbackButton.Style.PRIMARY,
            String.format("%s %s", ActionType.DELETE_TRIGGER.getActionId(), trigger.getTriggerId()), "Remove from space");

        PostbackButton backButton = new PostbackButton(PostbackButton.Style.SECONDARY,
            ActionType.GET_TRIGGERS.getActionId(), "Back");

        GenericAnnotation annotation = GenericAnnotation.builder()
            .text(builder.toString())
            .title(trigger.getTitle())
            .buttons(Arrays.asList(new GenericAnnotation.Button(deleteButton), new GenericAnnotation.Button(backButton)))
            .build();

        return new AnnotationWrapper(annotation);
    }

    private Attachment createCardForTrigger(Trigger trigger, Person creator) {

        String body = String.format("Added by %s", creator.getDisplayName());

        String buttonPayload = String.format("%s %s", ActionType.TRIGGER_INFO.getActionId(), trigger.getTriggerId());
        Button button = new Button(Button.ButtonStyle.PRIMARY, "See more", buttonPayload);

        InformationCard payload = InformationCard.builder()
            .date(UUIDs.unixTimestamp(trigger.getTriggerId()))
            .title(trigger.getTitle())
            .subtitle("trigger")
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
