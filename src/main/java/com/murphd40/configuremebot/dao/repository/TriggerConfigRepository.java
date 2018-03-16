package com.murphd40.configuremebot.dao.repository;

import java.util.UUID;

import com.murphd40.configuremebot.dao.model.TriggerConfig;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface TriggerConfigRepository extends CassandraRepository<TriggerConfig> {

    @Query("SELECT * FROM triggers_config WHERE triggerId = ?0")
    TriggerConfig findByTriggerId(UUID triggerId);

}
