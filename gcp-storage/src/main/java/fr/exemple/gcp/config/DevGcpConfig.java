package fr.exemple.gcp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.NoCredentials;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * Configuration pour utiliser l'émulateur GCP Storage en environnement de développement.
 * Cette classe configure le client Storage pour se connecter à l'émulateur local
 * plutôt qu'au service Google Cloud Storage réel.
 */
@Configuration
class DevGcpConfig {

    /**
     * Crée et configure un client Storage pour l'émulateur local.
     * Ce bean utilise des identifiants factices et pointe vers l'hôte de l'émulateur.
     *
     * @param projectIdProvider Fournisseur d'ID de projet GCP
     * @return Un client Storage configuré pour l'émulateur
     */
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
