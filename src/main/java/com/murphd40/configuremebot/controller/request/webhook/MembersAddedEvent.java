package com.murphd40.configuremebot.controller.request.webhook;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class MembersAddedEvent extends AbstractWebhookEvent {

    private String spaceName = "";
    private long time;
    private List<String> memberIds = Collections.emptyList();

    @JsonIgnore
    @Override
    public String getUserId() {
        return "";
    }

}
