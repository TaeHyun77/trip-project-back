package com.example.demo0810;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Demo0810Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo0810Application.class, args);
	}
}
