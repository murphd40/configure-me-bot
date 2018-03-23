package com.murphd40.configuremebot.service;

import java.util.Arrays;
import java.util.List;

import com.murphd40.configuremebot.controller.request.webhook.MembersAddedEvent;
import com.murphd40.configuremebot.controller.request.webhook.WebhookEvent;
import com.murphd40.configuremebot.dao.model.TriggerConfig;
import com.murphd40.configuremebot.event.EventContext;
import com.murphd40.configuremebot.event.TriggerActions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TriggerValidationService {

    public boolean validateTrigger(TriggerConfig triggerConfig) {
        try {
            Class<? extends WebhookEvent> eventClass = triggerConfig.getEventType().getEventClass();

            WebhookEvent webhookEvent = eventClass.getDeclaredConstructor().newInstance();

            if (webhookEvent instanceof MembersAddedEvent) {
                ((MembersAddedEvent) webhookEvent).setMemberIds(Arrays.asList("a", "b", "c"));
            }

            EventContext context = new EventContext(new DummyTriggerActions(), webhookEvent);
            ExpressionParser expressionParser = new SpelExpressionParser();
            EvaluationContext evaluationContext = new StandardEvaluationContext(context);

            if (triggerConfig.getCondition() != null) {
                Class<?> valueType = expressionParser.parseExpression(triggerConfig.getCondition()).getValueType(evaluationContext);
                boolean validCondition = Boolean.class.equals(valueType) && !containsIllegalMethods(triggerConfig.getCondition());

                if (!validCondition) {
                    return false;
                }
            }

            return null != expressionParser.parseExpression(triggerConfig.getAction()).getValueType(evaluationContext)
                && !containsIllegalMethods(triggerConfig.getAction());

        } catch (Exception e) {
            log.error("Error validating trigger: ", e);
            return false;
        }
    }

    // detects if an expression contains illegal method such as 'System.exit(0)'
    boolean containsIllegalMethods(String expression) {
        return expression.matches(".*T\\([^()]+\\).*");
    }

    // no-op version of DefaultTriggerActions used for validation
    static class DummyTriggerActions implements TriggerActions {
        @Override
        public void sendMessage(String content) {
            // no-op
        }

        @Override
        public void sendMessage(String content, String color) {
            // no-op
        }

        @Override
        public List<String> getNames(List<String> personIds) {
            return Arrays.asList("a", "b", "c");
        }

        @Override
        public List<String> toMentions(List<String> personIds) {
            return Arrays.asList("a", "b", "c");
        }
    }

}
