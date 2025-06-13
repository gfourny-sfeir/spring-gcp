package fr.exemple.gcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import fr.exemple.gcp.config.ApplicationProperties;

/**
 * Application principale pour le module GCP Pub/Sub.
 * Cette application écoute les événements de création de fichiers dans GCP Storage via Pub/Sub
 * et les enregistre dans Firestore.
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class GcpPubSubApplication {

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Arguments de ligne de commande passés à l'application
     */
    public static void main(String[] args) {
        SpringApplication.run(GcpPubSubApplication.class, args);
    }

}
