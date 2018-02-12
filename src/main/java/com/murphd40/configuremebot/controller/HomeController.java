package com.murphd40.configuremebot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by David on 09/02/2018.
 */
@Controller
public class HomeController {

    @GetMapping("/hello")
    public String helloWorld(Model model) {
        model.addAttribute("message", "hello world!");

        return "hello";
    }

}
