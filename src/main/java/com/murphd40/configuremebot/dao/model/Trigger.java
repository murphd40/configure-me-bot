package com.murphd40.configuremebot.dao.model;

import java.util.UUID;

import com.datastax.driver.core.DataType;
import com.murphd40.configuremebot.dao.EventType;
import lombok.Data;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

// todo - add triggers_by_event ((spaceId), eventType, id) MV
@Data
@Table("triggers")
public class Trigger {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String spaceId;

    @CassandraType(type = DataType.Name.TIMEUUID)
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1, ordering = Ordering.DESCENDING)
    private UUID id;

    @Column
    private EventType eventType;

    @Column
    private String title;

    @Column
    private String condition;

    @Column
    private String action;

    @Column
    private String creatorId;

}
