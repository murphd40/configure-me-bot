package com.murphd40.configuremebot.service;

import java.util.List;

import com.murphd40.configuremebot.controller.request.webhook.WebhookEvent;
import com.murphd40.configuremebot.dao.model.Trigger;
import com.murphd40.configuremebot.dao.repository.TriggerRepository;
import com.murphd40.configuremebot.event.DefaultTriggerActions;
import com.murphd40.configuremebot.event.EventContext;
import com.murphd40.configuremebot.event.EventType;
import com.murphd40.configuremebot.event.ThreadLocalContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class EventHandlerService {

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private DefaultTriggerActions triggerActions;

    public void processWebhookEvent(WebhookEvent event) {
        EventType eventType = EventType.fromString(event.getType());

        if (eventType == null) {
            // unsupported event
            return;
        }

        List<Trigger> triggers = triggerRepository.findBySpaceIdAndEventType(event.getSpaceId(), eventType);

        if (CollectionUtils.isEmpty(triggers)) {
            // no triggers
            return;
        }

        ThreadLocalContext.setSpaceId(event.getSpaceId());

        EventContext context = new EventContext(triggerActions, event);

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(context);

        triggers.stream()
            .peek(trigger -> ThreadLocalContext.setTriggerName(trigger.getTitle()))
            .filter(trigger -> {
                if (StringUtils.isEmpty(trigger.getCondition())) {
                    // no condition
                    return true;
                }

                return parser.parseExpression(trigger.getCondition()).getValue(evaluationContext, Boolean.class);
            })
            .forEach(trigger -> parser.parseExpression(trigger.getAction()).getValue(evaluationContext));

        ThreadLocalContext.clear();
    }

}
