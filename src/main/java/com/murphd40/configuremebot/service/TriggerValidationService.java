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

            EventContext context = new EventContext(new TriggerActions(), webhookEvent);
            ExpressionParser expressionParser = new SpelExpressionParser();
            EvaluationContext evaluationContext = new StandardEvaluationContext(context);

            if (triggerConfig.getCondition() != null) {
                Class<?> valueType = expressionParser.parseExpression(triggerConfig.getCondition()).getValueType(evaluationContext);
                boolean validCondition = Boolean.class.equals(valueType) && !containsIllegalMethods(triggerConfig.getCondition());

                if (!validCondition) {
                    return false;
                }
            }

            // todo - fix this!
            return null != expressionParser.parseExpression(triggerConfig.getAction()).getValueType(evaluationContext)
                && !containsIllegalMethods(triggerConfig.getAction());

        } catch (Exception e) {
            log.error("Error validating trigger: ", e);
            return false;
        }
    }

    boolean containsIllegalMethods(String expression) {
        return expression.matches(".*T\\([^()]+\\).*");
    }

}
