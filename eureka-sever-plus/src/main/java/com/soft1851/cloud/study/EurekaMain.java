package com.soft1851.cloud.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author xunmi
 * @ClassName EurekaMain
 * @Description TODO
 * @Date 2020/9/10
 * @Version 1.0
 **/
@SpringBootApplication
@EnableEurekaServer
public class EurekaMain {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMain.class, args);
    }

}
