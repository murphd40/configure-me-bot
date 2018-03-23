package com.murphd40.configuremebot.event;

import java.util.List;

public interface TriggerActions {
    void sendMessage(String content);

    void sendMessage(String content, String color);

    List<String> getNames(List<String> personIds);

    List<String> toMentions(List<String> personIds);
}
