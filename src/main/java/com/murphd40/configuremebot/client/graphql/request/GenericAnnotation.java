package com.murphd40.configuremebot.client.graphql.request;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GenericAnnotation {

    private String title;
    @NonNull
    private String text;
    private String color;
    private Actor actor;
    private List<Button> buttons;

    @Value
    public static class Actor {
        private String name;
    }

    @Value
    public static class Button {

        @NonNull
        private PostbackButton postbackButton;

        @Value
        public static class PostbackButton {

            @NonNull
            private Style style;
            @NonNull
            private String id;
            @NonNull
            private String title;

            public enum Style {
                PRIMARY,
                SECONDARY
            }

        }

    }

}
