package com.murphd40.configuremebot.client;

import com.murphd40.configuremebot.client.model.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WatsonWorkClient {

    @Headers({"Content-Type: application/json"})
    @POST("/v1/spaces/{spaceId}/messages")
    Call<Message> createMessage(@Header("Authorization") String authToken,
        @Path("spaceId") String spaceId, @Body Message message);

}