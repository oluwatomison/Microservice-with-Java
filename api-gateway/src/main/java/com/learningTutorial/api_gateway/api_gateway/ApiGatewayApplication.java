package com.learningTutorial.api_gateway.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}


	// Look at streaming, input buffer, output buffer (file handling)
	// Opening a socket
	// Using Dates - Yoda time / date-time-api (May not be the best choice)
	// Code organisation -  Using an Abstract class to isolate shared functionality. (Have 2 things that almost does the same thing)

}
