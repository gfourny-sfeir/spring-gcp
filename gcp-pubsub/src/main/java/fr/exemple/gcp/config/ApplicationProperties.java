package fr.exemple.gcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        /*
         * Nom de la souscription PubSub
         */
        @NotBlank String subscriptionName,
        /*
         * Nom de la collection Firestore dans laquelle sera enregistré les évènements PubSub
         */
        @NotBlank String collectionFirestore
) {
}
