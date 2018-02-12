package com.murphd40.configuremebot.configuration;

import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;

/**
 * Created by David on 11/02/2018.
 */
//@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "configureme";
    }

}
