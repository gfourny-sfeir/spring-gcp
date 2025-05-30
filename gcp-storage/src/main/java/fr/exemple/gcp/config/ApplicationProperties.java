package fr.exemple.gcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        /*
         * Nom du bucket d'écriture
         */
        @NotBlank String bucketName
) {
}
