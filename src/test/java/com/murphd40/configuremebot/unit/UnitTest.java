package com.murphd40.configuremebot.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphd40.configuremebot.controller.request.VerificationEvent;
import org.junit.Test;

/**
 * Created by David on 12/02/2018.
 */
public class UnitTest {

    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        VerificationEvent verificationEvent = new VerificationEvent();
        verificationEvent.setChallenge("challenge");
        verificationEvent.setType("type");

        System.out.println(mapper.writeValueAsString(verificationEvent));
    }

}
