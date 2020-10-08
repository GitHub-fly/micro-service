package com.soft1851.mysqlclient;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author xunmi
 */
@EnableEurekaClient
@MapperScan("com.soft1851.mysqlclient.mapper")
@SpringBootApplication
public class MysqlClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MysqlClientApplication.class, args);
	}

}
