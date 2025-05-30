package fr.exemple.gcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import fr.exemple.gcp.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class GcpStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcpStorageApplication.class, args);
	}

}
