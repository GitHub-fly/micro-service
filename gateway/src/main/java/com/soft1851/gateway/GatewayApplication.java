package com.soft1851.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZonedDateTime;

/**
 * @author xunmi
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
//        ZonedDateTime dateTime = ZonedDateTime.now();
//        System.out.println(dateTime);
    }

}
