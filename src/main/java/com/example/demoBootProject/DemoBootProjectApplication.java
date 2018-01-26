package com.example.demoBootProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoBootProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoBootProjectApplication.class, args);
	}

	@RestController
	class HomeController {

		@GetMapping("/")
		public String test() {
			return "Hola FantasyManager!!!!!!!!!!";
		}
	}
}
