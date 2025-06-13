package fr.exemple.gcp;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

/**
 * Gestionnaire des messages reçus via Google Cloud Pub/Sub.
 * Cette classe traite les notifications de création de fichiers dans GCP Storage
 * et les enregistre dans Firestore.
 */
@Component
public class MessageHandler {

    /**
     * Logger pour les messages de journalisation.
     */
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * Mapper JSON pour désérialiser les messages.
     */
    private final ObjectMapper objectMapper;

    /**
     * Service d'écriture dans Firestore.
     */
    private final FirestoreWriter firestoreWriter;

    /**
     * Constructeur pour l'injection des dépendances.
     *
     * @param objectMapper    Mapper JSON pour la désérialisation des messages
     * @param firestoreWriter Service pour écrire les données dans Firestore
     */
    MessageHandler(ObjectMapper objectMapper, FirestoreWriter firestoreWriter) {
        this.objectMapper = objectMapper;
        this.firestoreWriter = firestoreWriter;
    }

    /**
     * Traite un message reçu de Pub/Sub.
     * Extrait les informations de création de fichier, les enregistre dans Firestore
     * et acquitte le message.
     *
     * @param message Le message Pub/Sub à traiter
     */
    public void handle(Message<?> message) {
        try {
            var map = objectMapper.readValue(message.getPayload().toString(), FileCreated.class);
            log.info("Le fichier {} a été créé sur le bucket {}", map.name, map.bucket);
            firestoreWriter.save(Map.of("nom", map.name, "bucket", map.bucket));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional.of(message)
                .map(Message::getHeaders)
                .map(messageHeaders -> messageHeaders.get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class))
                .ifPresent(BasicAcknowledgeablePubsubMessage::ack);
    }

    /**
     * Structure de données représentant un événement de création de fichier.
     *
     * @param name   Nom du fichier créé
     * @param bucket Nom du bucket où le fichier a été créé
     */
    private record FileCreated(String name, String bucket) {
    }
}
