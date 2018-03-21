package com.murphd40.configuremebot.client.graphql;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class GraphQLQuery {
    private String query;
}
