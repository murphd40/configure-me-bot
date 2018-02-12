package com.murphd40.configuremebot.service;

import java.util.Base64;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphd40.configuremebot.WatsonWorkConstants;
import com.murphd40.configuremebot.client.AuthClient;
import com.murphd40.configuremebot.configuration.WatsonWorkspaceProperties;
import com.murphd40.configuremebot.controller.response.TokenResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by David on 11/02/2018.
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WatsonWorkspaceProperties workspaceProperties;

    private String appToken;
    private Date appTokenExpireTime;

    public String getAppAuthToken() {
        //if we never got the token or if the token is expired, set it
        if (appTokenExpireTime == null || appTokenExpireTime.before(new Date())) {
            try {
                TokenResponse tokenResponse = authClient.authenticateApp(createAppAuthHeader(), WatsonWorkConstants.CLIENT_CREDENTIALS).execute().body();
                appTokenExpireTime = getDate(tokenResponse.getExpiresIn());
                appToken = tokenResponse.getAccessToken();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return WatsonWorkConstants.BEARER + appToken;
    }

    private Date getDate(Integer secondsFromNow) {
        long millisFromNow = secondsFromNow * 1000L;
        return new Date(System.currentTimeMillis() + millisFromNow);
    }

    private String createAppAuthHeader() {
        WatsonWorkspaceProperties.App app = workspaceProperties.getApp();
        return WatsonWorkConstants.BASIC + Base64.getEncoder().encodeToString((app.getId() + ":" + app.getSecret()).getBytes());
    }

    @SneakyThrows
    public String createVerificationHeader(Object body) {
        String json = objectMapper.writeValueAsString(body);
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, workspaceProperties.getWebhook().getSecret()).hmacHex(json);
    }

    public boolean isValidVerificationRequest(Object requestBody, String outboundToken) {
        Assert.hasText(outboundToken, "outboundToken must not be blank");
        return createVerificationHeader(requestBody).equalsIgnoreCase(outboundToken);
    }

}
