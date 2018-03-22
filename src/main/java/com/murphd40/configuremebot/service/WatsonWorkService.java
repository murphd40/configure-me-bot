package com.murphd40.configuremebot.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphd40.configuremebot.client.WatsonWorkClient;
import com.murphd40.configuremebot.client.graphql.GraphQLQuery;
import com.murphd40.configuremebot.client.graphql.GraphQLQueryBuilder;
import com.murphd40.configuremebot.client.graphql.request.TargetedMessage;
import com.murphd40.configuremebot.client.graphql.response.Person;
import com.murphd40.configuremebot.client.model.Message;
import com.murphd40.configuremebot.configuration.WatsonWorkspaceProperties;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

/**
 * Created by David on 11/02/2018.
 */
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
    public void createMessage(String spaceId, Message message) {
        Response<Message> response = watsonWorkClient.createMessage(authService.getAppAuthToken(), spaceId, message).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Failed to send message");
        }
    }

    @SneakyThrows
    public void sendTargetedMessage(TargetedMessage targetedMessage) {
        GraphQLQuery query = graphQLQueryBuilder.buildTargetedMessageQuery(targetedMessage);
        watsonWorkClient.postGraphQLQuery(authService.getAppAuthToken(), query).execute();
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

}
