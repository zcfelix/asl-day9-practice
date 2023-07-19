package com.afs.restapi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping(value = "/{name}", params = {"language"})
    public String hello(@PathVariable String name,
                        @RequestParam String language) {
        if ("ZH".equals(language)) {
            return "你好： " + name;
        }
        return "hello: " + name;
    }
}
