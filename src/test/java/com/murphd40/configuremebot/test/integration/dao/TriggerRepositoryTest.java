package com.murphd40.configuremebot.test.integration.dao;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.datastax.driver.core.utils.UUIDs;
import com.murphd40.configuremebot.dao.model.Trigger;
import com.murphd40.configuremebot.dao.repository.TriggerRepository;
import com.murphd40.configuremebot.event.EventType;
import com.murphd40.configuremebot.test.integration.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TriggerRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TriggerRepository triggerRepository;

    @Test
    public void findTrigger() {
        Trigger trigger = createTrigger();

        triggerRepository.save(trigger);

        Trigger result = triggerRepository.findTrigger(trigger.getSpaceId(), trigger.getTriggerId());

        assertEquals(result, trigger);
    }

    @Test
    public void findBySpaceId() {
        String spaceId = UUIDs.random().toString();
        List<Trigger> triggers = Stream.generate(this::createTrigger).limit(3).collect(Collectors.toList());

        List<Trigger> targetTriggers = triggers.subList(1, 2);
        targetTriggers.forEach(trigger -> trigger.setSpaceId(spaceId));

        triggerRepository.save(triggers);

        List<Trigger> result = triggerRepository.findBySpaceId(spaceId);

        assertEquals(result.size(), targetTriggers.size());
        assertTrue(result.containsAll(targetTriggers));
    }

    @Test
    public void findByEventType() {
        String spaceId = UUIDs.random().toString();
        List<Trigger> triggers = Stream.generate(this::createTrigger).limit(3).collect(Collectors.toList());
        triggers.forEach(trigger -> trigger.setSpaceId(spaceId));

        EventType targetEventType = EventType.MESSAGE_ANNOTATION_ADDED;
        List<Trigger> targetTriggers = triggers.subList(1, 3);
        targetTriggers.forEach(trigger -> trigger.setEventType(targetEventType));

        triggerRepository.save(triggers);

        List<Trigger> result = triggerRepository.findBySpaceIdAndEventType(spaceId, targetEventType);

        assertEquals(result.size(), targetTriggers.size());
        assertTrue(result.containsAll(targetTriggers));
    }

    @Test
    public void deleteTrigger() {
        Trigger trigger = createTrigger();

        triggerRepository.save(trigger);

        assertNotNull(triggerRepository.findTrigger(trigger.getSpaceId(), trigger.getTriggerId()));

        boolean success = triggerRepository.deleteTrigger(trigger.getSpaceId(), trigger.getTriggerId());

        assertTrue(success);
        assertNull(triggerRepository.findTrigger(trigger.getSpaceId(), trigger.getTriggerId()));
    }

    @Test
    public void deleteTriggerWhichDoesNotExist() {
        assertFalse(triggerRepository.deleteTrigger("not-a-space", UUIDs.timeBased()));
    }

    @Test
    public void deletedTriggersCannotBeRetrievedFromTriggersByEventTable() {
        Trigger trigger = createTrigger();

        triggerRepository.save(trigger);

        List<Trigger> triggers = triggerRepository.findBySpaceIdAndEventType(trigger.getSpaceId(), trigger.getEventType());
        assertEquals(triggers, Collections.singletonList(trigger));

        triggerRepository.deleteTrigger(trigger.getSpaceId(), trigger.getTriggerId());

        triggers = triggerRepository.findBySpaceIdAndEventType(trigger.getSpaceId(), trigger.getEventType());
        assertTrue(triggers.isEmpty());
    }

    public Trigger createTrigger() {
        Trigger trigger = new Trigger();
        trigger.setAction("action");
        trigger.setCondition("condition");
        trigger.setCreatorId("creatorId");
        trigger.setEventType(EventType.MESSAGE_CREATED);
        trigger.setTitle("title");
        trigger.setSpaceId(UUIDs.random().toString());
        trigger.setTriggerId(UUIDs.timeBased());
        return trigger;
    }

}
