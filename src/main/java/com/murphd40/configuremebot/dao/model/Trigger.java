package com.murphd40.configuremebot.dao.model;

import java.util.UUID;

import com.datastax.driver.core.DataType;
import com.murphd40.configuremebot.event.EventType;
import lombok.Data;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Data
@Table("triggers")
public class Trigger {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String spaceId;

    @CassandraType(type = DataType.Name.TIMEUUID)
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1, ordering = Ordering.DESCENDING)
    private UUID triggerId;

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
