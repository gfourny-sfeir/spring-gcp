package fr.exemple.gcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Propriétés de configuration de l'application.
 * Cette classe contient les paramètres configurables de l'application chargés depuis le fichier de configuration.
 */
@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        /**
         * Nom de la souscription PubSub.
         * Identifie la souscription utilisée pour recevoir les messages de Pub/Sub.
         */
        @NotBlank String subscriptionName,

        /**
         * Nom de la collection Firestore.
         * Spécifie la collection dans laquelle seront enregistrés les événements Pub/Sub.
         */
        @NotBlank String collectionFirestore
) {
}
