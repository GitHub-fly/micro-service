package com.soft1851.consumer.hello.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author xunmi
 * @ClassName HelloHandler
 * @Description TODO
 * @Date 2020/9/13
 * @Version 1.0
 **/
@RequestMapping("/consumer")
@RestController
public class HelloHandler {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String getHello() {
        // 通过 restTemplate 来调用 provider 提供的服务
        return restTemplate.getForObject("http://jie.com:8001/hello", String.class);
    }
}
