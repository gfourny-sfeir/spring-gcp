package fr.exemple.gcp;

import java.time.Duration;
import java.util.stream.StreamSupport;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;

import fr.exemple.gcp.config.ApplicationProperties;
import io.vavr.Function0;
import io.vavr.Function1;

import static fr.exemple.gcp.FirestoreUtils.getAllDocumentsInCollection;
import static fr.exemple.gcp.FirestoreUtils.numberOfDocumentsInCollection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2EIT {

    @Autowired
    PubSubTemplate pubSubTemplate;
    @Autowired
    Firestore firestore;
    @Autowired
    ApplicationProperties applicationProperties;
    @Autowired
    ObjectMapper objectMapper;

    final Function0<FileCreated> fileCreatedPayload = () -> new FileCreated("Test Message", "commande-bucket");
    final Function1<FileCreated, String> toJson = fileCreated -> {
        try {
            return objectMapper.writeValueAsString(fileCreated);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    };

    @BeforeEach
    void clearFirestore() {
        final var documents = firestore.collection(applicationProperties.collectionFirestore()).listDocuments();
        StreamSupport.stream(documents.spliterator(), false)
                .map(DocumentReference::get)
                .map(FirestoreUtils::awaitDocumentSnapshot)
                .forEach(doc -> doc.getReference().delete());
    }

    @Test
    @DisplayName("☁️ Doit enregistrer dans Firestore l'évènement PubSub")
    void should_store_pubsub_event() {
        // Given ⌖
        var fileCreatedJson = fileCreatedPayload
                .andThen(toJson)
                .get();

        // When 👉
        pubSubTemplate.publish("commande", fileCreatedJson);

        // Then ✅
        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertThat(numberOfDocumentsInCollection(firestore, applicationProperties::collectionFirestore)).isEqualTo(1));

        var firstDoc = getAllDocumentsInCollection(firestore, applicationProperties::collectionFirestore).getFirst();

        assertThat(firstDoc).isNotNull()
                .extracting(DocumentSnapshot::getData)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Object.class))
                .containsKeys("bucket", "nom")
                .containsValue("commande-bucket");

    }

    private record FileCreated(String name, String bucket) {
    }
}
