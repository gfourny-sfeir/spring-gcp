package fr.exemple.gcp;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;

import fr.exemple.gcp.config.ApplicationProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(E2EIT.ITConfig.class)
class E2EIT {

    RestClient restClient;

    @LocalServerPort
    int port;

    @Autowired
    Storage storage;

    @Autowired
    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @DisplayName("📝 La commande doit être enregistrée dans le bucket GCP")
    void should_store_command_in_gcp_bucket() {
        // Given ⌖
        var commande = new Commande("123", "Test Commande", BigDecimal.ONE);

        // When 👉
        var entity = restClient.post()
                .uri("/api/commande")
                .body(commande)
                .retrieve()
                .toEntity(Commande.class);

        // Then ✅
        assertThat(entity)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody())
                .isNotNull()
                .extracting(Commande::id, Commande::nom, Commande::prix)
                .containsExactly(
                        "123",
                        "Test Commande",
                        BigDecimal.ONE
                );

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> assertThat(storage.list(applicationProperties.bucketName()).iterateAll())
                .hasSize(1));

        var blobList = storage.list(applicationProperties.bucketName()).streamAll().toList();

        assertThat(blobList)
                .hasSize(1)
                .extracting(Blob::getName)
                .containsExactly(
                        "Test Commande.txt"
                );

    }

    @TestConfiguration(proxyBeanMethods = false)
    public static class ITConfig {

        /**
         * Mock un bucket GCP
         *
         * @return Mock de l'interface GCP Storage
         */
        @Primary
        @Bean
        Storage storage() {
            return LocalStorageHelper.getOptions().getService();
        }

        @Bean
        CredentialsProvider googleCredentials() {
            return NoCredentialsProvider.create();
        }
    }
}
