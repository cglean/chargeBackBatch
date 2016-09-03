package com.chargeback.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class ChargeBackBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChargeBackBatchApplication.class, args);
	}
}
