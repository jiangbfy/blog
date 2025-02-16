package com.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/hello")
public class HelloController {
    private static final Integer OSHI_WAIT_SECOND = 1000;
    @RequestMapping(value = {"/hello"},method = RequestMethod.GET)
    public String say(){
        return "hello";
    }
}
