package com.murphd40.configuremebot.actionfulfillment;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActionParserTest {

    @Test
    public void validActionWithParameters() {
        String actionId = "/addTrigger param1 param2 param3";

        Action result = ActionParser.parseActionId(actionId);

        assertEquals(new Action(ActionType.ADD_TRIGGER, Arrays.asList("param1", "param2", "param3")), result);
    }

    @Test
    public void validActionNoParameters() {
        String actionId = "/addTrigger";

        Action result = ActionParser.parseActionId(actionId);

        assertEquals(new Action(ActionType.ADD_TRIGGER, Collections.emptyList()), result);
    }

    @Test
    public void invalidAction() {
        String actionId = "/invalid param1";

        Action result = ActionParser.parseActionId(actionId);

        assertEquals(new Action(null, Collections.singletonList("param1")), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankAction() {
        ActionParser.parseActionId(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAction() {
        ActionParser.parseActionId(null);
    }

}
