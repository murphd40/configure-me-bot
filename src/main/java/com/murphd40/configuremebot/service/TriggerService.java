package com.murphd40.configuremebot.service;

import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.utils.UUIDs;
import com.murphd40.configuremebot.dao.model.Trigger;
import com.murphd40.configuremebot.dao.model.TriggerConfig;
import com.murphd40.configuremebot.dao.repository.TriggerConfigRepository;
import com.murphd40.configuremebot.dao.repository.TriggerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TriggerService {

    @Autowired
    private TriggerRepository triggerRepository;
    @Autowired
    private TriggerConfigRepository triggerConfigRepository;
    @Autowired
    private TriggerValidationService triggerValidationService;

    public TriggerConfig createTrigger(TriggerConfig triggerConfig) {
        Assert.isTrue(triggerValidationService.validateTrigger(triggerConfig), "Invalid trigger");

        triggerConfig.setTriggerId(UUIDs.timeBased());

        return triggerConfigRepository.save(triggerConfig);
    }

    public boolean addTriggerToSpace(UUID triggerId, String spaceId, String userId) {
        TriggerConfig triggerConfig = triggerConfigRepository.findByTriggerId(triggerId);

        if (triggerConfig == null) {
            return false;
        }

        Trigger trigger = new Trigger();
        BeanUtils.copyProperties(triggerConfig, trigger);
        trigger.setSpaceId(spaceId);
        trigger.setCreatorId(userId);

        triggerRepository.save(trigger);

        return true;
    }

    public boolean deleteTriggerFromSpace(UUID triggerId, String spaceId) {
        return triggerRepository.deleteTrigger(spaceId, triggerId);
    }

    public List<Trigger> getTriggersForSpace(String spaceId) {
        return triggerRepository.findBySpaceId(spaceId);
    }

    public Trigger findTrigger(String spaceId, UUID triggerId) {
        return triggerRepository.findTrigger(spaceId, triggerId);
    }

}
