package com.murphd40.configuremebot.controller.request.webhook;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * Created by David on 21/10/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageFocusAnnotationPayload extends AnnotationPayload {

    @JsonCreator
    public static MessageFocusAnnotationPayload create(String json) throws IOException {
        return cleanJsonAndCreate(json, MessageFocusAnnotationPayload.class);
    }

    private String type;
    private String annotationId;
    private String version;
    private String created;
    private String createdBy;
    private String updated;
    private String updatedBy;
    private String tokenClientId;
    private String applicationId;
    private String lensId;
    private String lens;
    private String category;
    private String phrase;
    private List<String> actions;
    private double confidence;
    private Payload payload;
    private int start;
    private int end;
    private int focusVersion;
    private boolean hidden;
    private Context context;
    private JsonNode extractedInfo;

    @Data
    public static class Payload {
        private List<String> text;
        @JsonProperty("nodes_visited")
        private List<String> nodesVisited;
        private List<String> actions;
        @JsonProperty("log_messages")
        private List<String> logMessages;
    }

    @Data
    public static class Context {
        @JsonProperty("conversation_id")
        private String conversationId;
        private JsonNode system;
    }

//    @Data
//    public static class ExtractedInfo {
//
//    }
}
