package fr.exemple.gcp;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Représente une commande dans le système.
 * Cette classe contient toutes les informations nécessaires pour une commande.
 */
@Validated
public record Commande(
        /**
         * Identifiant unique de la commande.
         * Ne peut pas être vide ou null.
         */
        @NotBlank String id,

        /**
         * Nom de la commande.
         * Ne peut pas être vide ou null.
         */
        @NotBlank String nom,

        /**
         * Prix de la commande.
         * Ne peut pas être null.
         */
        @NotNull BigDecimal prix
) {
}
