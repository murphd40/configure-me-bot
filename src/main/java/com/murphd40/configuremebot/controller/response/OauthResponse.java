package com.murphd40.configuremebot.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by David on 11/02/2018.
 */

@Data
public class OauthResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private long expiresIn;
    private String scope;
    private String id;
    private String displayName;
    private String providerId;
    private String jti;
}
