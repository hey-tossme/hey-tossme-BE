package com.blackdragon.heytossme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class TestController {

    @GetMapping
    public String test() {
        log.info("Hello World! is processing...(cd test)");

        return "Hello world!";
    }
}
