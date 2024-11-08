package fr.exemple.gcp;

import org.springframework.stereotype.Service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import fr.exemple.gcp.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class CommandeSaver {

    private final Storage storage;
    private final ApplicationProperties properties;

    void save (Commande commande) {
        final var blobInfo = BlobInfo.newBuilder(properties.bucketName(), commande.nom()).build();
        storage.create(blobInfo, commande.toString().getBytes());
    }
}
