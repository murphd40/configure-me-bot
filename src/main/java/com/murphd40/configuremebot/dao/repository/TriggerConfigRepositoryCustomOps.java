package com.murphd40.configuremebot.dao.repository;

import com.murphd40.configuremebot.dao.model.TriggerConfig;

public interface TriggerConfigRepositoryCustomOps {

    TriggerConfig save(TriggerConfig entity);

    TriggerConfig insert(TriggerConfig entity);

    Iterable<TriggerConfig> save(Iterable<TriggerConfig> entities);

}
