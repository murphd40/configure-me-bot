package com.murphd40.configuremebot.dao;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "configuration";
    }

}
