package fr.exemple.gcp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.MessageChannel;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;

import fr.exemple.gcp.MessageHandler;

/**
 * Configuration pour l'intégration de Google Cloud Pub/Sub.
 * Cette classe configure les composants nécessaires pour recevoir et traiter
 * les messages provenant de Pub/Sub.
 */
@EnableIntegration
@Configuration
class PubSubConfig {

    /**
     * Crée un canal de messages pour les messages entrants de Pub/Sub.
     *
     * @return Un canal de publication/souscription pour les messages Pub/Sub
     */
    @Bean
    MessageChannel pubsubInputChannel() {
        return new PublishSubscribeChannel();
    }

    /**
     * Configure l'adaptateur de canal entrant pour Pub/Sub.
     * Cet adaptateur reçoit les messages de la souscription Pub/Sub spécifiée
     * et les envoie au canal d'entrée.
     *
     * @param inputChannel          Canal de messages pour recevoir les messages Pub/Sub
     * @param pubsubTemplate        Template pour interagir avec Pub/Sub
     * @param applicationProperties Propriétés de configuration de l'application
     * @return Un adaptateur de canal entrant configuré pour Pub/Sub
     */
    @Bean
    PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubsubInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubsubTemplate,
            ApplicationProperties applicationProperties
    ) {
        var adapter = new PubSubInboundChannelAdapter(pubsubTemplate, applicationProperties.subscriptionName());
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }

    /**
     * Configure le flux d'intégration pour traiter les messages Pub/Sub.
     * Ce flux transforme les messages reçus et les transmet au gestionnaire de messages.
     *
     * @param pubsubInputChannel Canal d'entrée pour les messages Pub/Sub
     * @param messageHandler     Gestionnaire qui traite les messages
     * @return Un flux d'intégration configuré
     */
    @Bean
    IntegrationFlow integrationFlow(MessageChannel pubsubInputChannel, MessageHandler messageHandler) {

        return IntegrationFlow.from(pubsubInputChannel)
                .transform(Transformers.objectToString())
                .handle(messageHandler::handle)
                .get();
    }
}
