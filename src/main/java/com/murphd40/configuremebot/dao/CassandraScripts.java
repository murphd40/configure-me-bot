package com.murphd40.configuremebot.dao;

import java.util.ArrayList;
import java.util.List;

final class CassandraScripts {

    private static final String CREATE_TRIGGER_TABLE =
        "CREATE TABLE IF NOT EXISTS %s.triggers (" +
            "spaceId text," +
            "triggerId timeuuid," +
            "action text," +
            "condition text," +
            "creatorId text," +
            "eventType text," +
            "title text," +
            "PRIMARY KEY ((spaceId), triggerId)" +
            ") WITH CLUSTERING ORDER BY (triggerId DESC);";

    private static final String CREATE_TRIGGERS_BY_EVENT_MV =
        "CREATE MATERIALIZED VIEW IF NOT EXISTS %s.triggers_by_event AS " +
            "SELECT * FROM triggers " +
            "WHERE spaceId IS NOT NULL AND triggerId IS NOT NULL AND eventType IS NOT NULL " +
            "PRIMARY KEY ((spaceId, eventType), triggerId) " +
            "WITH CLUSTERING ORDER BY (triggerId DESC)";

    public static List<String> getCreateScriptsInExecutionOrder(String keyspace) {
        List<String> scripts = new ArrayList<>();
        scripts.add(String.format(CREATE_TRIGGER_TABLE, keyspace));
        scripts.add(String.format(CREATE_TRIGGERS_BY_EVENT_MV, keyspace));
        return scripts;
    }
}
