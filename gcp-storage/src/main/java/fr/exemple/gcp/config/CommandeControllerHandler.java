package fr.exemple.gcp.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.cloud.storage.StorageException;

@ControllerAdvice
class CommandeControllerHandler {

    @ExceptionHandler(StorageException.class)
    ResponseEntity<String> handleStorageException(StorageException ex) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "5")
                .body(ex.getMessage());
    }
}
