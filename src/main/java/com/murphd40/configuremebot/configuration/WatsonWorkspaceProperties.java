package com.murphd40.configuremebot.configuration;

import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by David on 11/02/2018.
 */
@Data
@ConfigurationProperties(prefix = "watsonwork")
public class WatsonWorkspaceProperties {

    @NotNull
    private Api api;
    @NotNull
    private Webhook webhook;
    @NotNull
    private App app;

    @Data
    public static class Api {
        @NotBlank
        private String uri;
        @NotBlank
        private String oauth;
    }

    @Data
    public static class Webhook {
        @NotBlank
        private String secret;
    }

    @Data
    public static class App {
        @NotBlank
        private String id;
        @NotBlank
        private String secret;
    }

}
