package com.murphd40.configuremebot.actionfulfillment;

import java.util.Arrays;

public enum ActionType {
    GET_TRIGGERS("/triggers"),
    DELETE_TRIGGER("deleteTrigger"),
    ADD_TRIGGER("/addTrigger"),
    TRIGGER_INFO("triggerInfo");

    private String actionId;

    ActionType(String actionId) {
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }

    public static ActionType fromActionId(String actionId) {
        return Arrays.stream(values()).filter(actionType -> actionType.actionId.equals(actionId)).findFirst().orElse(null);
    }
}
