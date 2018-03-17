package com.murphd40.configuremebot.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphd40.configuremebot.controller.request.webhook.VerificationEvent;
import lombok.Data;
import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Created by David on 12/02/2018.
 */
public class UnitTest {

    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        VerificationEvent verificationEvent = new VerificationEvent();
        verificationEvent.setChallenge("challenge");
        verificationEvent.setType("type");

        System.out.println(mapper.writeValueAsString(verificationEvent));
    }

    @Test
    public void foo() throws NoSuchMethodException {
        MessageCreated event = new MessageCreated();
        event.setContent("hello world");
        event.setSenderId("000");

        State state = new State();
        state.setStatus("assemble-team");

        Context context = new Context();
        context.setEvent(event);
        context.setState(state);

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(context);
        evaluationContext.registerFunction("foo", UnitTest.class.getMethod("sendMessage", String.class));

        String condition = "event.content.contains('hello') && state.status == 'moop'";
        String action = "#foo('moop')";

        Boolean value = parser.parseExpression(condition).getValue(evaluationContext, Boolean.class);

        if (value) {
            parser.parseExpression(action).getValue(evaluationContext);
        }
    }

    @Data
    public static class Context {
        private Event event;
        private State state;
    }

    public interface Event {
    }

    @Data
    public static class MessageCreated implements Event {
        public static final String type = "message-created";

        private String content;
        private String senderId;
    }

    @Data
    public static class MemberAdded implements Event {
        public static final String type = "member-added";

        private String memberId;
        private String senderId;
    }

    @Data
    public static class State {
        private String status;
    }

    public static void sendMessage(String message) {
        System.out.println(message);
    }

}
