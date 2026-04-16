package com.example.walletsim_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WalletsimBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletsimBackendApplication.class, args);
	}

}
