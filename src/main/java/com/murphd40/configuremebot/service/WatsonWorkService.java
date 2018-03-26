package com.murphd40.configuremebot.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphd40.configuremebot.client.WatsonWorkClient;
import com.murphd40.configuremebot.client.graphql.GraphQLQuery;
import com.murphd40.configuremebot.client.graphql.GraphQLQueryBuilder;
import com.murphd40.configuremebot.client.graphql.request.Message;
import com.murphd40.configuremebot.client.graphql.request.TargetedMessage;
import com.murphd40.configuremebot.client.graphql.response.Person;
import com.murphd40.configuremebot.configuration.WatsonWorkspaceProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 11/02/2018.
 */
@Slf4j
@Service
public class WatsonWorkService {

    @Autowired
    private WatsonWorkspaceProperties workspaceProperties;

    @Autowired
    private WatsonWorkClient watsonWorkClient;

    @Autowired
    private AuthService authService;

    @Autowired
    private GraphQLQueryBuilder graphQLQueryBuilder;

    @SneakyThrows
    public void sendMessage(Message message) {
        GraphQLQuery query = graphQLQueryBuilder.buildMessageQuery(message);
        watsonWorkClient.postGraphQLQuery(authService.getAppAuthToken(), query).enqueue(logCallback());
    }

    @SneakyThrows
    public void sendTargetedMessage(TargetedMessage targetedMessage) {
        GraphQLQuery query = graphQLQueryBuilder.buildTargetedMessageQuery(targetedMessage);
        watsonWorkClient.postGraphQLQuery(authService.getAppAuthToken(), query).enqueue(logCallback());
    }

    @SneakyThrows
    public List<Person> getPeople(List<String> ids) {
        GraphQLQuery graphQLQuery = graphQLQueryBuilder.buildGetPeopleQuery(ids);
        Response<JsonNode> response = watsonWorkClient.postGraphQLQuery(authService.getAppAuthToken(), graphQLQuery).execute();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode items = response.body().findValue("items");

        List<Person> people = new ArrayList<>();
        for (JsonNode node : items) {
            Person person = mapper.treeToValue(node, Person.class);
            people.add(person);
        }

        return people;
    }

    private <T> Callback<T> logCallback() {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                log.info("Request: {}, Response: {}", call.request(), response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                log.error("Failed request.", t);
            }
        };
    }

}
