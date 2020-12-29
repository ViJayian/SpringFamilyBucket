package org.vijayian.controller;

import entity.Hello;
import org.springframework.web.bind.annotation.*;

/**
 * hello.
 *
 * @author ViJay
 * @date 2020-12-28
 */
@RestController
public class HelloController {

    @GetMapping(value = {"/helloPathVariable/{id}/{name}", "/helloPathVariable"})
    public Hello helloPathVariable(@PathVariable(required = false, value = "id") Integer id, @PathVariable(required = false, value = "name") String name) {
        return Hello.builder(id, name);
    }

    @RequestMapping(value = "/helloRequestParam", method = {RequestMethod.GET, RequestMethod.POST})
    public Hello helloRequestParam(@RequestParam Integer id) {
        return Hello.builder(id);
    }

    @PostMapping("/helloRequestBody")
    public Hello helloRequestBody(@RequestBody Hello hello) {
        return hello;
    }

    @GetMapping("/helloDefault")
    public Hello helloDefault(Integer id, String name) {
        return Hello.builder(id, name);
    }
}