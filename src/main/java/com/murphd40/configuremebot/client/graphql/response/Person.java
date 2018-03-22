package com.murphd40.configuremebot.client.graphql.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Person {
    private String displayName;
    private String email;
    @JsonProperty("extId")
    private String id;
}
