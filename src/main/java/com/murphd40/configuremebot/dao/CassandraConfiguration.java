package com.murphd40.configuremebot.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "configuration";
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<String> getStartupScripts() {
        List<String> startupScripts = new ArrayList<>();

        startupScripts.add(CassandraScripts.CREATE_TRIGGER_TABLE);
        startupScripts.add(CassandraScripts.CREATE_TRIGGERS_BY_EVENT_MV);

        return startupScripts;
    }
}
