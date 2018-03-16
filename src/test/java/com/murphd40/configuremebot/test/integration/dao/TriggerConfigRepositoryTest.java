package com.murphd40.configuremebot.test.integration.dao;

import java.util.concurrent.TimeUnit;

import com.datastax.driver.core.utils.UUIDs;
import com.google.common.util.concurrent.Uninterruptibles;
import com.murphd40.configuremebot.dao.model.TriggerConfig;
import com.murphd40.configuremebot.dao.repository.TriggerConfigRepository;
import com.murphd40.configuremebot.event.EventType;
import com.murphd40.configuremebot.test.integration.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TriggerConfigRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TriggerConfigRepository triggerConfigRepository;

    @Value("${TRIGGER_CONFIG_TTL}")
    private int ttl;

    @Test
    public void findByTriggerId() {
        TriggerConfig triggerConfig = createTriggerConfig();

        triggerConfigRepository.save(triggerConfig);

        TriggerConfig result = triggerConfigRepository.findByTriggerId(triggerConfig.getTriggerId());

        assertEquals(triggerConfig, result);
    }

    @Test
    public void entryDeletedAfterTtl() {
        TriggerConfig triggerConfig = createTriggerConfig();

        triggerConfigRepository.save(triggerConfig);

        Uninterruptibles.sleepUninterruptibly(ttl, TimeUnit.SECONDS);
        Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);

        TriggerConfig result = triggerConfigRepository.findByTriggerId(triggerConfig.getTriggerId());

        assertNull(result);
    }

    private TriggerConfig createTriggerConfig() {
        TriggerConfig triggerConfig = new TriggerConfig();

        triggerConfig.setTriggerId(UUIDs.timeBased());
        triggerConfig.setAction("action");
        triggerConfig.setCondition("condition");
        triggerConfig.setEventType(EventType.MESSAGE_CREATED);
        triggerConfig.setTitle("title");

        return triggerConfig;
    }

}
