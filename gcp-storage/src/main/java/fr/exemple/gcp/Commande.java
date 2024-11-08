package fr.exemple.gcp;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Commande(
        @NotBlank String id,
        @NotBlank String nom,
        @NotNull BigDecimal prix
) {
}
