package com.murphd40.configuremebot.event;

import com.murphd40.configuremebot.client.model.Message;
import com.murphd40.configuremebot.service.WatsonWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TriggerActions {

    @Autowired
    private WatsonWorkService watsonWorkService;

    public void sendMessage(String content) {
        sendMessage(content, null, null);
    }

    public void sendMessage(String content, String color, String title) {
        String spaceId = ThreadLocalContext.getSpaceId();
        Message message = Message.appMessage(content);
        message.getAnnotations().get(0).setColor(color);
        message.getAnnotations().get(0).setTitle(title);
        watsonWorkService.createMessage(spaceId, message);
    }

}
