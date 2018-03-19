package com.murphd40.configuremebot.controller.request.webhook;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by David on 21/10/2017.
 */
public abstract class AnnotationPayload {

    protected static <T> T cleanJsonAndCreate(String json, Class<T> tClass) throws IOException {
        String cleanJson = json
            .replace("\\", "")
            .replace("\"{", "{")
            .replace("}\"", "}");

        JsonNode jsonNode = new ObjectMapper().reader().readTree(cleanJson);

        return new ObjectMapper().treeToValue(jsonNode, tClass);
    }

}
