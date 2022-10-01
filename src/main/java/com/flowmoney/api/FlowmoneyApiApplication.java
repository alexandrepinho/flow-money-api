package com.flowmoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.flowmoney.api.config.property.FlowMoneyProperty;

@SpringBootApplication
@EnableConfigurationProperties(FlowMoneyProperty.class)
public class FlowmoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowmoneyApiApplication.class, args);
	}

}
