package com.soft1851.user.center.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xunmi
 * @ClassName UserController
 * @Description TODO
 * @Date 2020/9/23
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/hello")
    public String getData() {
        log.info("我被调用了");
        return "Hello, I'm from user-center";
    }
}
