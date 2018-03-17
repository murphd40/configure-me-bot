package com.murphd40.configuremebot.event;

public class ThreadLocalContext {

    private static final ThreadLocal<String> spaceIdThreadLocal = new ThreadLocal<>();

    public static void setSpaceId(String spaceId) {
        spaceIdThreadLocal.set(spaceId);
    }

    public static String getSpaceId() {
        return spaceIdThreadLocal.get();
    }

    public static void clear() {
        spaceIdThreadLocal.remove();
    }

}
