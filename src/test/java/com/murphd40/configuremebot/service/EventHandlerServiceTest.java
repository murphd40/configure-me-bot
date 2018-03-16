package com.murphd40.configuremebot.service;

import java.util.Collections;

import com.murphd40.configuremebot.controller.request.MessageCreatedEvent;
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
import org.mockito.Spy;

import static org.mockito.Matchers.any;

public class EventHandlerServiceTest {

    @InjectMocks
    private EventHandlerService eventHandlerService = new EventHandlerService();

    @Spy
    private TriggerActions triggerActions;

    @Mock
    private TriggerRepository triggerRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() {
        Trigger trigger = new Trigger();
        trigger.setAction("actions.sendMessage('Daylight come and I wanna go home ' + event.type)");

        Mockito.when(triggerRepository.findBySpaceIdAndEventType(any(), any())).thenReturn(Collections.singletonList(trigger));

        MessageCreatedEvent event = new MessageCreatedEvent();
        event.setType(EventType.MESSAGE_CREATED.getValue());

        eventHandlerService.processWebhookEvent(event);
    }

}
