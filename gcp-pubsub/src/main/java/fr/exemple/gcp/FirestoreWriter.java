package fr.exemple.gcp;

import java.util.Map;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.Firestore;

@Service
public class FirestoreWriter {

    private static final Logger log = LoggerFactory.getLogger(FirestoreWriter.class);

    private final Firestore firestore;

    public FirestoreWriter(Firestore firestore) {
        this.firestore = firestore;
    }

    public void save(Map<String, String> message) {
        try {
            firestore.collection("pubsub")
                    .document()
                    .set(message)
                    .addListener(() -> log.info("Le message {} a bien été enregistré", message.get("nom")), Executors.newVirtualThreadPerTaskExecutor());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
