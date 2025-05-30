package fr.exemple.gcp;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.Firestore;

import fr.exemple.gcp.config.ApplicationProperties;

@Service
public class FirestoreWriter {

    private static final Logger log = LoggerFactory.getLogger(FirestoreWriter.class);

    private final Firestore firestore;
    private final ApplicationProperties applicationProperties;
    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    public FirestoreWriter(Firestore firestore, ApplicationProperties applicationProperties) {
        this.firestore = firestore;
        this.applicationProperties = applicationProperties;
    }

    public void save(Map<String, String> message) {
        firestore.collection(applicationProperties.collectionFirestore())
                .document()
                .set(message)
                .addListener(() -> log.info("Le message {} a bien été enregistré", message.get("nom")), executor);
    }
}
