package fr.exemple.gcp;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public record Commande(
        @NotBlank String id,
        @NotBlank String nom,
        @NotNull BigDecimal prix
) {
}
