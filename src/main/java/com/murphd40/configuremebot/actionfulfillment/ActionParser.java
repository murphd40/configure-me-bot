package com.murphd40.configuremebot.actionfulfillment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionParser {

    public static Action parseActionId(String actionId) {
        Assert.hasText(actionId, "actionId must not be blank");

        List<String> fragments = Arrays.stream(actionId.split("\\s")).collect(Collectors.toList());

        ActionType actionType = ActionType.fromActionId(fragments.get(0));

        return new Action(actionType, fragments.subList(1, fragments.size()));
    }

}
