package com.soft1851.consumer.hello.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xunmi
 * @ClassName MySqlHandler
 * @Description TODO
 * @Date 2020/9/13
 * @Version 1.0
 **/
@RequestMapping("/api")
@RestController
public class MySqlHandler {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/all")
    public List<?> getAll() {
        return restTemplate.getForObject("http://jie.com:8002/all", List.class);
    }
}
