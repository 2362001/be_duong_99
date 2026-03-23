package vn.be.platform_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlatformServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformServiceApplication.class, args);
	}

}
