package fr.exemple.gcp;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.Firestore;

import fr.exemple.gcp.config.ApplicationProperties;

/**
 * Service responsable de l'écriture des messages dans Firestore.
 * Cette classe gère la persistance des données dans la base de données Firestore de Google Cloud Platform.
 */
@Service
public class FirestoreWriter {

    /**
     * Logger pour les messages de journalisation.
     */
    private static final Logger log = LoggerFactory.getLogger(FirestoreWriter.class);

    /**
     * Instance de Firestore pour accéder à la base de données.
     */
    private final Firestore firestore;

    /**
     * Propriétés de l'application contenant la configuration.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * Exécuteur pour les opérations asynchrones utilisant les threads virtuels.
     */
    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Constructeur pour l'injection des dépendances.
     *
     * @param firestore             Instance de Firestore pour accéder à la base de données
     * @param applicationProperties Propriétés de configuration de l'application
     */
    public FirestoreWriter(Firestore firestore, ApplicationProperties applicationProperties) {
        this.firestore = firestore;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Sauvegarde un message dans Firestore.
     * Le message est enregistré dans la collection spécifiée dans les propriétés de l'application.
     *
     * @param message Map contenant les données du message à sauvegarder
     */
    public void save(Map<String, String> message) {
        firestore.collection(applicationProperties.collectionFirestore())
                .document()
                .set(message)
                .addListener(() -> log.info("Le message {} a bien été enregistré", message.get("nom")), executor);
    }
}
