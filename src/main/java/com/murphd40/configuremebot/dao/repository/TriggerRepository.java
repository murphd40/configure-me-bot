package com.murphd40.configuremebot.dao.repository;

import java.util.List;
import java.util.UUID;

import com.murphd40.configuremebot.dao.model.Trigger;
import com.murphd40.configuremebot.event.EventType;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface TriggerRepository extends CassandraRepository<Trigger> {

    @Query("SELECT * FROM triggers WHERE spaceId = ?0 AND triggerId = ?1")
    Trigger findTrigger(String spaceId, UUID triggerId);

    @Query("SELECT * FROM triggers WHERE spaceId = ?0")
    List<Trigger> findBySpaceId(String spaceId);

    @Query("SELECT * FROM triggers_by_event WHERE spaceId = ?0 AND eventType = ?1")
    List<Trigger> findBySpaceIdAndEventType(String spaceId, EventType eventType);

    @Query("DELETE FROM triggers WHERE spaceId = ?0 AND triggerId = ?1")
    void deleteTrigger(String spaceId, UUID triggerId);

}
