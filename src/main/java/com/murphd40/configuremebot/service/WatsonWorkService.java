package com.murphd40.configuremebot.service;

import com.murphd40.configuremebot.client.WatsonWorkClient;
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

    @SneakyThrows
    public void createMessage(String spaceId, Message message) {
        Response<Message> response = watsonWorkClient.createMessage(authService.getAppAuthToken(), spaceId, message).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Failed to send message");
        }
    }

}