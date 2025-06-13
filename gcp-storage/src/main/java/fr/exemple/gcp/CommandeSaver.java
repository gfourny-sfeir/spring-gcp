package fr.exemple.gcp;

import org.springframework.stereotype.Service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import fr.exemple.gcp.config.ApplicationProperties;

/**
 * Service responsable de la sauvegarde des commandes dans Google Cloud Storage.
 * Cette classe gère l'enregistrement des objets Commande sous forme de fichiers texte
 * dans un bucket GCS spécifié dans les propriétés de l'application.
 */
@Service
class CommandeSaver {

    private final Storage storage;
    private final ApplicationProperties properties;

    /**
     * Constructeur du service CommandeSaver.
     *
     * @param storage    l'instance de Storage pour accéder à Google Cloud Storage
     * @param properties les propriétés de l'application contenant la configuration du bucket
     */
    CommandeSaver(Storage storage, ApplicationProperties properties) {
        this.storage = storage;
        this.properties = properties;
    }

    /**
     * Sauvegarde une commande dans Google Cloud Storage.
     * La commande est enregistrée sous forme de fichier texte avec le nom de la commande comme nom de fichier.
     *
     * @param commande {@link Commande} la commande à sauvegarder
     */
    void save(Commande commande) {
        final var blobInfo = BlobInfo.newBuilder(properties.bucketName(), commande.nom() + ".txt").build();
        storage.create(blobInfo, commande.toString().getBytes());
    }
}
