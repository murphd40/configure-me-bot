package com.murphd40.configuremebot.event;

public class ThreadLocalContext {

    private static final ThreadLocal<String> spaceIdThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> triggerNameThreadLocal = new ThreadLocal<>();

    public static void setSpaceId(String spaceId) {
        spaceIdThreadLocal.set(spaceId);
    }

    public static String getSpaceId() {
        return spaceIdThreadLocal.get();
    }

    public static void setTriggerName(String triggerName) {
        triggerNameThreadLocal.set(triggerName);
    }

    public static String getTriggerName() {
        return triggerNameThreadLocal.get();
    }

    public static void clear() {
        spaceIdThreadLocal.remove();
    }

}
