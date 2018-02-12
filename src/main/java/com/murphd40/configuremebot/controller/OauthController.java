package com.murphd40.configuremebot.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by David on 11/02/2018.
 */
@Controller
public class OauthController {

    @GetMapping("/oauthCallback")
    public ResponseEntity<String> oauthCallback(@RequestParam OauthRequestParams requestParams, Map<String, Object> model,
        HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok("foo");
    }

    @Value
    public static class OauthRequestParams {
        private String code;
        private String state;
    }

}
