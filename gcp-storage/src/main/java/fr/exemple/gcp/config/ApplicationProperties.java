package fr.exemple.gcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        /*
         * Nom du bucket d'écriture
         */
        @NotBlank String bucketName
) {
}
