package com.soft1851.content.center;

import com.soft1851.content.center.service.TestService;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author xunmi
 * @ClassName TestMain
 * @Description TODO
 * @Date 2020/10/6
 * @Version 1.0
 **/
public class TestMain {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 100; i++) {
            String obj = restTemplate.getForObject("http://localhost:8881/notice/one", String.class);
            System.out.println("ok");
            Thread.sleep(200);
        }
    }
}
