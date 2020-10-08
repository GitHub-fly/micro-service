package com.soft1851.cloud.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.swing.*;

/**
 * @author xunmi
 * @ClassName ClientMain
 * @Description TODO
 * @Date 2020/9/10
 * @Version 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class ClientMain {
    public static void main(String[] args) {
        SpringApplication.run(ClientMain.class, args);
    }
}
