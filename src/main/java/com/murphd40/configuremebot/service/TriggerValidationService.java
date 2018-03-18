package com.murphd40.configuremebot.service;

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

    // no-op version of TriggerActions used for validation
    static class DummyTriggerActions extends TriggerActions {
        @Override
        public void sendMessage(String content) {
            // no-op
        }

        @Override
        public void sendMessage(String content, String color, String title) {
            // no-op
        }
    }

}
