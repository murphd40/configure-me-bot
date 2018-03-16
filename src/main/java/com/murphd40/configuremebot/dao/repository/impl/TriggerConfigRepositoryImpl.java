package com.murphd40.configuremebot.dao.repository.impl;

import java.util.Collections;

import com.murphd40.configuremebot.dao.model.TriggerConfig;
import com.murphd40.configuremebot.dao.repository.TriggerConfigRepositoryCustomOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.core.CassandraOperations;

public class TriggerConfigRepositoryImpl implements TriggerConfigRepositoryCustomOps {

    @Autowired
    private CassandraOperations cassandraOperations;

    @Value("${data.cassandra.trigger-config.ttl-in-seconds}")
    private int ttlInSeconds;

    @Override
    public TriggerConfig save(TriggerConfig entity) {
        return save(Collections.singleton(entity)).iterator().next();
    }

    @Override
    public TriggerConfig insert(TriggerConfig entity) {
        return save(entity);
    }

    @Override
    public Iterable<TriggerConfig> save(Iterable<TriggerConfig> entities) {

        for (TriggerConfig triggerConfig : entities) {
            cassandraOperations.insert(triggerConfig, WriteOptions.builder().ttl(ttlInSeconds).build());
        }

        return entities;
    }

}
