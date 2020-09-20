package com.soft1851.provider.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xunmi
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProviderHelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderHelloApplication.class, args);
	}

}
