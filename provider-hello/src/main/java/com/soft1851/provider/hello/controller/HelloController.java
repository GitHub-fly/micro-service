package com.soft1851.provider.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xunmi
 * @ClassName HelloController
 * @Description TODO
 * @Date 2020/9/13
 * @Version 1.0
 **/
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String getString() {
        return "Hello Spring Cloud";
    }
}
