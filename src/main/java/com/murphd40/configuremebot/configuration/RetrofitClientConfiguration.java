package com.murphd40.configuremebot.configuration;

import com.murphd40.configuremebot.client.AuthClient;
import com.murphd40.configuremebot.client.WatsonWorkClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by David on 11/02/2018.
 */
@Configuration
public class RetrofitClientConfiguration {

    @Autowired
    private WatsonWorkspaceProperties watsonWorkspaceProperties;

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(watsonWorkspaceProperties.getApi().getUri())
            .client(new OkHttpClient())
            .build();
    }

    @Bean
    public AuthClient authClient() {
        return retrofit().create(AuthClient.class);
    }

    @Bean
    public WatsonWorkClient watsonWorkClient() {
        return retrofit().create(WatsonWorkClient.class);
    }

}
