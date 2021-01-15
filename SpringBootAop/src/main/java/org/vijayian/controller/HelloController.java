package org.vijayian.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vijayian.entity.Hello;

/**
 * hello.
 *
 * @author ViJay
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(String name) {
        return name;
    }

    @PostMapping("/hello")
    public String getHelloName(@RequestBody Hello hello){
        return hello.getName();
    }
}
