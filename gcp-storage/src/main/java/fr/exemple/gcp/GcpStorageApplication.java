package fr.exemple.gcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import fr.exemple.gcp.config.ApplicationProperties;

/**
 * Application principale pour le module GCP Storage.
 * Cette application permet de sauvegarder et lister des commandes dans Google Cloud Storage.
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class GcpStorageApplication {

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Arguments de ligne de commande passés à l'application
     */
    public static void main(String[] args) {
        SpringApplication.run(GcpStorageApplication.class, args);
    }

}
