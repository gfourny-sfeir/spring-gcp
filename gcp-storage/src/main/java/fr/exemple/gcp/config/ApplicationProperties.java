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
         * Nom du bucket d'écriture dans Google Cloud Storage.
         * Spécifie le bucket dans lequel les commandes seront sauvegardées.
         */
        @NotBlank String bucketName
) {
}
