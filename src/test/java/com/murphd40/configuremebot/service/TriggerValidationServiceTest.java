package com.murphd40.configuremebot.service;

import com.murphd40.configuremebot.dao.model.TriggerConfig;
import com.murphd40.configuremebot.event.EventType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TriggerValidationServiceTest {

    private TriggerValidationService triggerValidationService = new TriggerValidationService();

    @Test
    public void containsIllegalMethodsStartOfString() {
        String expression = "T(java.lang.System).exit(0)";

        assertTrue(triggerValidationService.containsIllegalMethods(expression));
    }

    @Test
    public void containsIllegalMethodsMiddleOfString() {
        String expression = "lalalallaT(java.lang.System).exit(0)";

        assertTrue(triggerValidationService.containsIllegalMethods(expression));
    }

    @Test
    public void doesNotContainIllegalMethods() {
        String expression = "actions.sendMessage('hello world')";

        assertFalse(triggerValidationService.containsIllegalMethods(expression));
    }

    @Test
    public void validAction() {
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setEventType(EventType.MESSAGE_CREATED);
        triggerConfig.setAction("actions.sendMessage('hello world')");

        assertTrue(triggerValidationService.validateTrigger(triggerConfig));
    }

    @Test
    public void badExpression() {
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setEventType(EventType.MESSAGE_CREATED);
        triggerConfig.setAction("notAMethod()");

        assertFalse(triggerValidationService.validateTrigger(triggerConfig));
    }

    @Test
    public void fieldsDoNotMatchEventType() {
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setEventType(EventType.MESSAGE_ANNOTATION_ADDED);
        triggerConfig.setAction("actions.sendMessage(event.content)");

        assertFalse(triggerValidationService.validateTrigger(triggerConfig));
    }

}
