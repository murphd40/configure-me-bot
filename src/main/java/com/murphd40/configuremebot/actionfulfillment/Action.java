package com.murphd40.configuremebot.actionfulfillment;

import java.util.List;

import lombok.Value;

@Value
public class Action {

    private ActionType type;
    private List<String> params;

}
