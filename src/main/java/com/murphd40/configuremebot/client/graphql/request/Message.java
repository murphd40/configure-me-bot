package com.murphd40.configuremebot.client.graphql.request;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Created by David on 25/03/2018.
 */
@Value
@Builder
public class Message {

    @NonNull
    private String conversationId;

    private String content;

    private List<AnnotationWrapper> annotations;

}
