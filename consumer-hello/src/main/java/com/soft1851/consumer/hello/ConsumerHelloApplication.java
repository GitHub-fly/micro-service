package com.soft1851.consumer.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author xunmi
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerHelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerHelloApplication.class, args);
    }


    /**
     * 创建 RestTemplate 实例并通过 @Bean 注解注入到 IoC 容器中
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
