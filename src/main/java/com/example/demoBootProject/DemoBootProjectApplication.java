package com.example.demoBootProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "fantasymanager" })
@EntityScan("fantasymanager.data")
@EnableJpaRepositories("fantasymanager.repository")
public class DemoBootProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoBootProjectApplication.class, args);
	}
}
