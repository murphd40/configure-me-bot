package com.murphd40.configuremebot.event;

import java.util.List;
import java.util.stream.Collectors;

import com.murphd40.configuremebot.client.graphql.response.Person;
import com.murphd40.configuremebot.client.model.Message;
import com.murphd40.configuremebot.service.WatsonWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultTriggerActions implements TriggerActions {

    @Autowired
    private WatsonWorkService watsonWorkService;

    @Override
    public void sendMessage(String content) {
        sendMessage(content, "#FF0000");
    }

    @Override
    public void sendMessage(String content, String color) {
        String spaceId = ThreadLocalContext.getSpaceId();
        String triggerName = ThreadLocalContext.getTriggerName();
        Message message = Message.appMessage(content);
        message.getAnnotations().get(0).setColor(color);
        message.getAnnotations().get(0).setTitle(triggerName);
        watsonWorkService.createMessage(spaceId, message);
    }

    @Override
    public List<String> getNames(List<String> personIds) {
        return watsonWorkService.getPeople(personIds).stream().map(Person::getDisplayName).collect(Collectors.toList());
    }

    @Override
    public List<String> toMentions(List<String> personIds) {
        return watsonWorkService.getPeople(personIds).stream()
            .map(person -> String.format("<@%s|%s>", person.getId(), person.getDisplayName()))
            .collect(Collectors.toList());
    }
}
