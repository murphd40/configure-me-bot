package com.murphd40.configuremebot.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.murphd40.configuremebot.client.graphql.GraphQLQuery;
import com.murphd40.configuremebot.client.model.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WatsonWorkClient {

    @Headers("Content-Type: application/json")
    @POST("/v1/spaces/{spaceId}/messages")
    Call<Message> createMessage(@Header("Authorization") String authToken,
        @Path("spaceId") String spaceId, @Body Message message);

    @POST("/graphql")
    @Headers({"Content-Type: application/json", "x-graphql-view: PUBLIC, BETA"})
    Call<JsonNode> sendTargetedMessage(@Header("Authorization") String authToken, @Body GraphQLQuery body);

//    @POST("/graphql")
//    @Headers({"Content-Type: application/json", "x-graphql-view: BETA"})
//    Call<String> sendMessage(@Header("Authorization") String authToken, @Body String body);

}