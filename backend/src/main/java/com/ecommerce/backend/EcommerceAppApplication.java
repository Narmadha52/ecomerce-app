package com.ecommerce.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The main application class for the Spring Boot E-commerce Backend.
 *
 * @SpringBootApplication: Meta-annotation that includes @Configuration, @EnableAutoConfiguration, and @ComponentScan.
 * @EnableJpaRepositories: Explicitly enables JPA repositories, although often auto-configured.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ecommerce.backend")
public class EcommerceAppApplication {

	public static void main(String[] args) {
		// Runs the Spring application, creating the ApplicationContext and starting the embedded server.
		SpringApplication.run(EcommerceAppApplication.class, args);
	}

}
