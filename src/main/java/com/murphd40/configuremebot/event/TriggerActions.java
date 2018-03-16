package com.murphd40.configuremebot.event;

import org.springframework.stereotype.Component;

@Component
public class TriggerActions {

    public void sendMessage(String content) {
        System.out.println(content);
    }

}
