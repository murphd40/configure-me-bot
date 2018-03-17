package com.murphd40.configuremebot.controller.request.trigger;

import javax.validation.constraints.NotNull;

import com.murphd40.configuremebot.event.EventType;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CreateTrigger {

    @NotNull
    private EventType eventType;
    @NotBlank
    private String title;
    @NotBlank
    private String action;

    private String condition;

}
