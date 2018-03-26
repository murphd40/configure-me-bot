package com.murphd40.configuremebot.event;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.murphd40.configuremebot.client.graphql.request.AnnotationWrapper;
import com.murphd40.configuremebot.client.graphql.request.GenericAnnotation;
import com.murphd40.configuremebot.client.graphql.request.Message;
import com.murphd40.configuremebot.client.graphql.response.Person;
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

        AnnotationWrapper wrapper = new AnnotationWrapper(
            GenericAnnotation.builder()
                .text(content)
                .title(triggerName)
                .color(color)
                .build()
        );

        Message message = Message.builder()
            .conversationId(spaceId)
            .annotations(Collections.singletonList(wrapper))
            .build();

        watsonWorkService.sendMessage(message);
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
