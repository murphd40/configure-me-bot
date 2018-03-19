package com.murphd40.configuremebot.controller;

import javax.validation.Valid;

import com.murphd40.configuremebot.controller.request.trigger.CreateTrigger;
import com.murphd40.configuremebot.dao.model.TriggerConfig;
import com.murphd40.configuremebot.service.TriggerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trigger")
public class TriggerController {

    @Autowired
    private TriggerService triggerService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<TriggerConfig> configureTrigger(@Valid @RequestBody CreateTrigger createTrigger) {

        TriggerConfig triggerConfig = new TriggerConfig();
        BeanUtils.copyProperties(createTrigger, triggerConfig);

        triggerConfig = triggerService.createTrigger(triggerConfig);

        return ResponseEntity.ok(triggerConfig);
    }
}
