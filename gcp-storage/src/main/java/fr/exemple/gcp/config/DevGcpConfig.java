package fr.exemple.gcp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.NoCredentials;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * Uniquement pour utiliser l'émulateur GCP
 */
@Configuration
class DevGcpConfig {

    @Bean
    Storage storage(GcpProjectIdProvider projectIdProvider) {
        return StorageOptions.newBuilder()
                .setCredentials(NoCredentials.getInstance())
                .setProjectId(projectIdProvider.getProjectId())
                .setHost("http://storage:4443")
                .build()
                .getService();
    }
}
