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

@Component
public class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final ObjectMapper objectMapper;
    private final FirestoreWriter firestoreWriter;

    MessageHandler(ObjectMapper objectMapper, FirestoreWriter firestoreWriter) {
        this.objectMapper = objectMapper;
        this.firestoreWriter = firestoreWriter;
    }

    public void handle(Message<?> message){
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

    private record FileCreated(String name, String bucket) {
    }
}
