package org.vijayian.boot.controller;

import entity.Hello;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * HelloController.
 * 请求规则：
 *  GET: url(/hello/{id}/{name})
 *  GET/POST: url(/hello?id=&name=)
 *  POST: RequestBody(json)
 *  POST: MultipartFile(Requestparm)
 *
 *
 * @author ViJay
 * @date 2020-12-24
 */
//@RestController
public class HelloController {


    @GetMapping(value = {"/helloPathVariable/{id}/{name}", "/helloPathVariable"})
    void helloPathVariable(@PathVariable(required = false, value = "id") Integer id,
                           @PathVariable(required = false, value = "name") String name) {
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

    @PostMapping("/hellFile")
    public Hello helloFile(@RequestParam(name = "file") MultipartFile file) {
        return Hello.builder(1);
    }
}
