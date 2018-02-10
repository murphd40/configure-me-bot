package com.murphd40.configuremebot.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by David on 09/02/2018.
 */
@Controller
public class HomeController {

    @GetMapping
    public String helloWorld(Model model) {

        String message = String.format("Hello! The current time is %s", new Date());

        model.addAttribute("message", message);

        return "hello";
    }

}
