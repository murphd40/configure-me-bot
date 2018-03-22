package com.murphd40.configuremebot.client.graphql.request;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class InformationCard implements CardPayload {

    @NonNull
    private String title;
    @NonNull
    private String subtitle;
    @NonNull
    private String text;
    @NonNull
    private Long date;

    private List<Button> buttons;

    @Value
    public static class Button {

        @NonNull
        private ButtonStyle style;
        @NonNull
        private String text;
        @NonNull
        private String payload;

        public enum ButtonStyle {
            PRIMARY,
            SECONDARY
        }

    }

}
