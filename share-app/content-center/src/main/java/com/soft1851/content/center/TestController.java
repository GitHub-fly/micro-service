package com.soft1851.content.center;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

/**
 * @author xunmi
 * @ClassName TestController
 * @Description TODO
 * @Date 2020/9/23
 * @Version 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/test")

public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/data")
    public List<ServiceInstance> getInstances() {
        return this.discoveryClient.getInstances("user-center");
    }

    @GetMapping("/call/hello")
    public String callUserCenter() {
        // 用户中心所有的实例信息
        List<ServiceInstance> instances = discoveryClient.getInstances(("user-center"));
        ServiceInstance serviceInstance = instances.get(new Random().nextInt(instances.size()));
        String targetUrl = serviceInstance.getUri() + "/user/hello";
        // stream 编程、Lambda 表达式、函数式编程
//        String targetUrl = instances.stream()
//                .map(instance -> instance.getUri().toString() + "/user/hello")
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("当前没有实例！"));
        log.info("请求的目标地址：{}", targetUrl);
        return restTemplate.getForObject(targetUrl, String.class);
    }

    @GetMapping("/call/ribbon")
    public String call() {
        return restTemplate.getForObject("http://user-center/user/hello", String.class);
    }

}
