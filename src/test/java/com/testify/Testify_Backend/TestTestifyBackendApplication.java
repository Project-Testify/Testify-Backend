package com.testify.Testify_Backend;

import org.springframework.boot.SpringApplication;

public class TestTestifyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(TestifyBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
