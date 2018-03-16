package com.murphd40.configuremebot.dao;

final class CassandraScripts {

    static final String CREATE_TRIGGER_TABLE =
        "CREATE TABLE IF NOT EXISTS triggers (" +
            "spaceId text," +
            "triggerId timeuuid," +
            "action text," +
            "condition text," +
            "creatorId text," +
            "eventType text," +
            "title text," +
            "PRIMARY KEY ((spaceId), triggerId)" +
            ") WITH CLUSTERING ORDER BY (triggerId DESC);";

    static final String CREATE_TRIGGERS_BY_EVENT_MV =
        "CREATE MATERIALIZED VIEW triggers_by_event AS" +
            "SELECT * FROM triggers " +
            "WHERE spaceId IS NOT NULL AND triggerId IS NOT NULL AND eventType IS NOT NULL " +
            "PRIMARY KEY ((spaceId, eventType), triggerId) " +
            "WITH CLUSTERING ORDER BY (triggerId DESC)";
}
