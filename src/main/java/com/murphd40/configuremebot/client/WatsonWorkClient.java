package com.murphd40.configuremebot.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.murphd40.configuremebot.client.graphql.GraphQLQuery;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WatsonWorkClient {

    @POST("/graphql")
    @Headers({"Content-Type: application/json", "x-graphql-view: PUBLIC, BETA"})
    Call<JsonNode> postGraphQLQuery(@Header("Authorization") String authToken, @Body GraphQLQuery body);

}