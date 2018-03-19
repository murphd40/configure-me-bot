package com.murphd40.configuremebot.client.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Message {

    private String id;
    private String content;
    private String type;
    private double version;
    private List<Annotation> annotations;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Annotation {

        private String type;
        private double version;
        private String color;
        private String title;
        private String text;
//    private Actor actor;
    }

    public static Message appMessage(String content) {
        Annotation annotation = new Annotation();
        annotation.setText(content);
        annotation.setType("generic");
        Message message = new Message();
        message.setType("appMessage");
        message.setVersion(1);
        message.setAnnotations(Collections.singletonList(annotation));
        return message;
    }
}