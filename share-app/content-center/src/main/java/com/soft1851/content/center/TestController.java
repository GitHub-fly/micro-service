package com.soft1851.content.center;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.soft1851.content.center.domain.dto.UserDTO;
import com.soft1851.content.center.feignclient.TestFeignClient;
import com.soft1851.content.center.feignclient.TestGitHubFeignClient;
import com.soft1851.content.center.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
@Api(tags = "测试接口", value = "提供测试相关的 Rest API")
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    @GetMapping("/data")
    @ApiOperation(value = "获取所有实例", notes = "得到所有的实例")
    public List<ServiceInstance> getInstances() {
        return this.discoveryClient.getInstances("user-center");
    }

    @GetMapping("/call/hello")
    @ApiOperation(value = "调用不同实例的方法", notes = "调用不同实例的方法")
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
    @ApiOperation(value = "通过ribbon来调用方法", notes = "通过ribbon来调用方法")
    public String call() {
        return restTemplate.getForObject("http://user-center/user/hello", String.class);
    }


    private final TestFeignClient testFeignClient;

    @ApiOperation(value = "通过feign来调用方法", notes = "通过feign来调用方法")
    @GetMapping("/test-q")

    public UserDTO getUser(UserDTO userDTO) {
        return testFeignClient.getUser(userDTO);
    }

    private final TestGitHubFeignClient testGitHubFeignClient;

    @GetMapping("/github")
    @ApiOperation(value = "通过feign来调用未注册的服务", notes = "通过feign来调用未注册的服务")
    public String github() {
        return testGitHubFeignClient.index();
    }


    private final TestService testService;

    @GetMapping("/test-a")
    public String testA() {
        testService.commonMethod();
        return "test-a";
    }

    @GetMapping("/test-b")
    public String testB() {
        testService.commonMethod();
        return "test-b";
    }

    @GetMapping("byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public String byResource() {
        return "按名称限流";
    }

    public String handleException(BlockException blockException) {
        return "服务不可用";
    }
}

