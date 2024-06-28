package com.testify.Testify_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TestifyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestifyBackendApplication.class, args);
	}

}
