package fr.exemple.gcp.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.cloud.storage.StorageException;

/**
 * Gestionnaire global des exceptions pour les contrôleurs.
 * Cette classe intercepte les exceptions spécifiques et les transforme en réponses HTTP appropriées.
 */
@ControllerAdvice
class CommandeControllerHandler {

    /**
     * Gère les exceptions liées au stockage Google Cloud Storage.
     * Retourne une réponse HTTP 503 (Service Unavailable) avec un en-tête Retry-After.
     *
     * @param ex L'exception de stockage interceptée
     * @return Une réponse HTTP avec le message d'erreur
     */
    @ExceptionHandler(StorageException.class)
    ResponseEntity<String> handleStorageException(StorageException ex) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "5")
                .body(ex.getMessage());
    }
}
