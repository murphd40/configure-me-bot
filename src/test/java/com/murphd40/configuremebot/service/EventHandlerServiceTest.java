package com.murphd40.configuremebot.service;

import java.util.Collections;
import java.util.UUID;

import com.murphd40.configuremebot.controller.request.webhook.MessageCreatedEvent;
import com.murphd40.configuremebot.dao.model.Trigger;
import com.murphd40.configuremebot.dao.repository.TriggerRepository;
import com.murphd40.configuremebot.event.EventType;
import com.murphd40.configuremebot.event.TriggerActions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class EventHandlerServiceTest {

    @InjectMocks
    private EventHandlerService eventHandlerService = new EventHandlerService();

    @Mock
    private TriggerActions triggerActions;

    @Mock
    private TriggerRepository triggerRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void executeAction() {
        Trigger trigger = createTrigger();

        Mockito.when(triggerRepository.findBySpaceIdAndEventType(any(), any())).thenReturn(Collections.singletonList(trigger));

        MessageCreatedEvent event = createEvent();

        eventHandlerService.processWebhookEvent(event);

        Mockito.verify(triggerActions).sendMessage(Mockito.eq("Hello world"));
    }

    @Test
    public void conditionPasses() {
        Trigger trigger = createTrigger();
        trigger.setCondition("event.content.contains('world')");

        Mockito.when(triggerRepository.findBySpaceIdAndEventType(any(), any())).thenReturn(Collections.singletonList(trigger));

        MessageCreatedEvent event = createEvent();

        eventHandlerService.processWebhookEvent(event);

        Mockito.verify(triggerActions).sendMessage(anyString());
    }

    @Test
    public void conditionFails() {
        Trigger trigger = createTrigger();
        trigger.setCondition("event.content.contains('unicorn')");

        Mockito.when(triggerRepository.findBySpaceIdAndEventType(any(), any())).thenReturn(Collections.singletonList(trigger));

        MessageCreatedEvent event = createEvent();

        eventHandlerService.processWebhookEvent(event);

        Mockito.verify(triggerActions, Mockito.never()).sendMessage(anyString());
    }

    @Test
    public void noTriggersForEvent() {
        Mockito.when(triggerRepository.findBySpaceIdAndEventType(any(), any())).thenReturn(Collections.emptyList());

        eventHandlerService.processWebhookEvent(createEvent());

        Mockito.verify(triggerActions, Mockito.never()).sendMessage(anyString());
    }

    @Test
    public void eventTypeNotSupported() {
        Trigger trigger = createTrigger();

        Mockito.when(triggerRepository.findBySpaceIdAndEventType(any(), any())).thenReturn(Collections.singletonList(trigger));

        MessageCreatedEvent event = createEvent();
        event.setType("unknown-type");

        eventHandlerService.processWebhookEvent(event);

        Mockito.verify(triggerActions, Mockito.never()).sendMessage(Mockito.anyString());
    }

    private Trigger createTrigger() {
        Trigger trigger = new Trigger();
        trigger.setAction("actions.sendMessage(event.content)");
        return trigger;
    }

    private MessageCreatedEvent createEvent() {
        MessageCreatedEvent event = new MessageCreatedEvent();
        event.setType(EventType.MESSAGE_CREATED.getValue());
        event.setContent("Hello world");
        event.setSpaceId(UUID.randomUUID().toString());
        return event;
    }

}
